package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.encoding.WasmEncoder;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class WasmBinaryEncoder implements WasmEncoder {
    static final byte[] WASM_MAGIC = new byte[]{0x00, 0x61, 0x71, 0x6D};
    static final byte[] WASM_BINARY_FORMAT_VERSION = new byte[]{0x01, 0x00, 0x00, 0x00};

    private void encodeSection(OutputStream output, SectionId sectionId, byte[] content) throws IOException {
        output.write(sectionId.id());
        LEB128.writeUnsigned(output, content.length);
        output.write(content);
    }

    @Override
    public void encodeTypes(OutputStream output, List<FuncType> types) throws IOException {
        var intermediate = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(intermediate);

        LEB128.writeUnsigned(intermediate, types.size());
        for (FuncType type : types) {
            visitor.visitFuncType(type);
        }

        encodeSection(output, SectionId.TYPE, intermediate.toByteArray());
    }

    @Override
    public void encodeFunctions(OutputStream output, List<Func> funcs) throws IOException {

    }

    @Override
    public void encodeTables(OutputStream output, List<Table> tables) throws IOException {

    }

    @Override
    public void encodeMemories(OutputStream output, List<MemType> mems) throws IOException {

    }

    @Override
    public void encodeGlobals(OutputStream output, List<Global> globals) throws IOException {

    }

    @Override
    public void encodeElems(OutputStream output, List<Elem> elems) throws IOException {

    }

    @Override
    public void encodeData(OutputStream output, List<Data> datas) throws IOException {

    }

    @Override
    public void encodeStart(OutputStream output, Integer start) throws IOException {

    }

    @Override
    public void encodeImports(OutputStream output, List<Import> imports) throws IOException {

    }

    @Override
    public void encodeExports(OutputStream output, List<Export> imports) throws IOException {

    }

    public void encodeMagic(OutputStream output) throws IOException {
        output.write(WASM_MAGIC);
    }

    public void encodeVersion(OutputStream output) throws IOException {
        output.write(WASM_BINARY_FORMAT_VERSION);
    }

    public void encodeDataCount(OutputStream output, List<Data> datas) throws IOException {
        LEB128.writeUnsigned(output, datas.size());
    }

    public void encodeCode(OutputStream output, List<Func> funcs) throws IOException {

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
