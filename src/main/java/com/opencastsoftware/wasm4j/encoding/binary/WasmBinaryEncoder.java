package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.encoding.WasmEncoder;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;

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
            var visitor = new WasmTypeBinaryEncodingVisitor(intermediate);

            LEB128.writeUnsigned(intermediate, types.size());
            for (FuncType type : types) {
                visitor.visitFuncType(type);
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
            var visitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x01);
            visitor.visitTableType(table.tableType());
        } else if (descriptor instanceof Import.Descriptor.Mem) {
            var mem = (Import.Descriptor.Mem) descriptor;
            var visitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x02);
            visitor.visitMemType(mem.memType());
        } else if (descriptor instanceof Import.Descriptor.Global) {
            var global = (Import.Descriptor.Global) descriptor;
            var visitor = new WasmTypeBinaryEncodingVisitor(output);
            output.write(0x03);
            visitor.visitGlobalType(global.globalType());
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
            for (Func func: funcs) {
                LEB128.writeUnsigned(intermediate, func.typeIndex());
            }

            encodeSection(output, SectionId.FUNCTION, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeTables(OutputStream output, List<Table> tables) throws IOException {
        if (!tables.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            encodeSection(output, SectionId.TABLE, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeMemories(OutputStream output, List<MemType> mems) throws IOException {
        if (!mems.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            encodeSection(output, SectionId.MEMORY, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeGlobals(OutputStream output, List<Global> globals) throws IOException {
        if (!globals.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            encodeSection(output, SectionId.GLOBAL, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeExports(OutputStream output, List<Export> imports) throws IOException {
        if (!imports.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

            encodeSection(output, SectionId.IMPORT, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeStart(OutputStream output, Integer start) throws IOException {
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

            encodeSection(output, SectionId.CODE, intermediate.toByteArray());
        }
    }

    @Override
    public void encodeData(OutputStream output, List<Data> datas) throws IOException {
        if (!datas.isEmpty()) {
            var intermediate = new ByteArrayOutputStream();

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
