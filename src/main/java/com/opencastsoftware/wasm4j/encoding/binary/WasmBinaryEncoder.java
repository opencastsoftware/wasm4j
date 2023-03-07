package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.encoding.WasmEncoder;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WasmBinaryEncoder implements WasmEncoder {
    static final byte[] WASM_MAGIC = new byte[]{0x00, 0x61, 0x71, 0x6D};
    static final byte[] WASM_BINARY_FORMAT_VERSION = new byte[]{0x01, 0x00, 0x00, 0x00};

    private void encodeSection(OutputStream output, SectionId sectionId, byte[] content) throws IOException {
        output.write(sectionId.id());
        LEB128.writeUnsigned(output, content.length);
        output.write(content);
    }

    private void encodeString(OutputStream output, String string) throws IOException {
        var bytes = string.getBytes(StandardCharsets.UTF_8);
        LEB128.writeUnsigned(output, bytes.length);
        output.write(bytes);
    }

    public void encodeMagic(OutputStream output) throws IOException {
        output.write(WASM_MAGIC);
    }

    public void encodeVersion(OutputStream output) throws IOException {
        output.write(WASM_BINARY_FORMAT_VERSION);
    }

    @Override
    public void encodeTypes(OutputStream output, List<FuncType> types) throws IOException {
        if (!types.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(intermediate);

            LEB128.writeUnsigned(intermediate, types.size());
            for (FuncType type : types) {
                typeVisitor.visitFuncType(type);
            }

            encodeSection(output, SectionId.TYPE, intermediate.toByteArray());
        }
    }

    private void encodeImportDescriptor(OutputStream output, Import.Descriptor descriptor) throws IOException {
        if (descriptor instanceof Import.Descriptor.Func) {
            var func = (Import.Descriptor.Func) descriptor;
            output.write(0x00);
            LEB128.writeUnsigned(output, func.typeIndex());
        } else if (descriptor instanceof Import.Descriptor.Table) {
            var table = (Import.Descriptor.Table) descriptor;
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x01);
            typeVisitor.visitTableType(table.tableType());
        } else if (descriptor instanceof Import.Descriptor.Mem) {
            var mem = (Import.Descriptor.Mem) descriptor;
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x02);
            typeVisitor.visitMemType(mem.memType());
        } else if (descriptor instanceof Import.Descriptor.Global) {
            var global = (Import.Descriptor.Global) descriptor;
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x03);
            typeVisitor.visitGlobalType(global.globalType());
        }
    }

    @Override
    public void encodeImports(OutputStream output, List<Import> imports) throws IOException {
        if (!imports.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            LEB128.writeUnsigned(intermediate, imports.size());
            for (Import imp : imports) {
                encodeString(intermediate, imp.module());
                encodeString(intermediate, imp.name());
                encodeImportDescriptor(intermediate, imp.descriptor());
            }

            encodeSection(output, SectionId.IMPORT, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeFunctions(OutputStream output, List<Func> funcs) throws IOException {
        if (!funcs.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            LEB128.writeUnsigned(intermediate, funcs.size());
            for (Func func : funcs) {
                LEB128.writeUnsigned(intermediate, func.typeIndex());
            }

            encodeSection(output, SectionId.FUNCTION, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeTables(OutputStream output, List<Table> tables) throws IOException {
        if (!tables.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(intermediate);
            var constExprVisitor = new ConstantInstructionBinaryEncodingVisitor(intermediate, typeVisitor);

            LEB128.writeUnsigned(intermediate, tables.size());
            for (Table table : tables) {
                // TODO: Handle shorthand table declaration - needs instruction equals implementations
                // if (table.type().refType().isNullable() &&
                //         table.init().instructions().isEmpty()) {
                //     table.type().accept(typeVisitor);
                // } else {
                intermediate.write(0x40);
                intermediate.write(0x00);
                table.type().accept(typeVisitor);
                table.init().accept(constExprVisitor);
                // }
            }

            encodeSection(output, SectionId.TABLE, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeMemories(OutputStream output, List<MemType> mems) throws IOException {
        if (!mems.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(intermediate);

            LEB128.writeUnsigned(intermediate, mems.size());
            for (MemType mem : mems) {
                typeVisitor.visitMemType(mem);
            }

            encodeSection(output, SectionId.MEMORY, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeGlobals(OutputStream output, List<Global> globals) throws IOException {
        if (!globals.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(intermediate);
            var constExprVisitor = new ConstantInstructionBinaryEncodingVisitor(intermediate, typeVisitor);

            LEB128.writeUnsigned(intermediate, globals.size());
            for (Global global : globals) {
                global.type().accept(typeVisitor);
                global.init().accept(constExprVisitor);
            }

            encodeSection(output, SectionId.GLOBAL, intermediate.toByteArray());
        }
    }

    private void encodeExportDescriptor(OutputStream output, Export.Descriptor descriptor) throws IOException {
        switch (descriptor.indexType()) {
            case FUNC:
                output.write(0x00);
                break;
            case TABLE:
                output.write(0x01);
                break;
            case MEM:
                output.write(0x02);
                break;
            case GLOBAL:
                output.write(0x03);
                break;
        }

        LEB128.writeUnsigned(output, descriptor.index());
    }

    @Override
    public void encodeExports(OutputStream output, List<Export> exports) throws IOException {
        if (!exports.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            LEB128.writeUnsigned(intermediate, exports.size());
            for (Export export : exports) {
                encodeString(intermediate, export.name());
                encodeExportDescriptor(intermediate, export.descriptor());
            }

            encodeSection(output, SectionId.EXPORT, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeStart(OutputStream output, @Nullable Integer start) throws IOException {
        if (start != null) {
            var intermediate = new ByteArrayOutputStream();
            LEB128.writeUnsigned(intermediate, start);
            encodeSection(output, SectionId.START, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeElems(OutputStream output, List<Elem> elems) throws IOException {
        if (!elems.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            LEB128.writeUnsigned(intermediate, elems.size());
            for (Elem elem : elems) {

            }

            encodeSection(output, SectionId.ELEMENT, intermediate.toByteArray());
        }
    }

    public void encodeDataCount(OutputStream output, List<Data> datas) throws IOException {
        if (!datas.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            LEB128.writeUnsigned(intermediate, datas.size());
            encodeSection(output, SectionId.DATA_COUNT, intermediate.toByteArray());
        }
    }

    public void encodeCode(OutputStream output, List<Func> funcs) throws IOException {
        if (!funcs.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            LEB128.writeUnsigned(intermediate, funcs.size());
            for (Func func : funcs) {
                // TODO: Needs expression encoding visitor
            }

            encodeSection(output, SectionId.CODE, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeData(OutputStream output, List<Data> datas) throws IOException {
        if (!datas.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();
            var typeVisitor = new WasmTypeBinaryEncodingVisitor(intermediate);
            var constExprVisitor = new ConstantInstructionBinaryEncodingVisitor(intermediate, typeVisitor);

            LEB128.writeUnsigned(intermediate, datas.size());
            for (Data data : datas) {
                // Could be replaced with a bitfield if more flags are added
                int indicator;

                if (data.mode() instanceof Data.Mode.Passive) {
                    // The 1st bit of the indicator encodes whether this is a passive data segment.
                    indicator = 0b01;
                    LEB128.writeUnsigned(intermediate, indicator);

                } else if (data.mode() instanceof Data.Mode.Active) {
                    var active = (Data.Mode.Active) data.mode();

                    if (active.memIndex() != 0) {
                        // The 2nd bit of the indicator indicates whether there's an explicit
                        // memory index for an active segment.
                        indicator = 0b10;
                        LEB128.writeUnsigned(intermediate, indicator);
                        LEB128.writeUnsigned(intermediate, active.memIndex());
                    } else {
                        // In this case, we have an active segment with no explicit memory index.
                        indicator = 0b00;
                        LEB128.writeUnsigned(intermediate, indicator);
                    }

                    active.offset().accept(constExprVisitor);
                }

                LEB128.writeUnsigned(intermediate, data.init().length);
                intermediate.write(data.init());
            }

            encodeSection(output, SectionId.DATA, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeModule(OutputStream output, Module module) throws IOException {
        encodeMagic(output);
        encodeVersion(output);
        encodeTypes(output, module.types());
        encodeImports(output, module.imports());
        encodeFunctions(output, module.funcs());
        encodeTables(output, module.tables());
        encodeMemories(output, module.mems());
        encodeGlobals(output, module.globals());
        encodeExports(output, module.exports());
        encodeStart(output, module.start());
        encodeElems(output, module.elems());
        encodeDataCount(output, module.datas());
        encodeCode(output, module.funcs());
        encodeData(output, module.datas());
    }
}
