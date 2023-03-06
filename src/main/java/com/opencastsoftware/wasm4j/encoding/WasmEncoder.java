package com.opencastsoftware.wasm4j.encoding;

import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface WasmEncoder {
    void encodeTypes(OutputStream output, List<FuncType> types) throws IOException;
    void encodeFunctions(OutputStream output, List<Func> funcs) throws IOException;
    void encodeTables(OutputStream output, List<Table> tables) throws IOException;
    void encodeMemories(OutputStream output, List<MemType> mems) throws IOException;
    void encodeGlobals(OutputStream output, List<Global> globals) throws IOException;
    void encodeElems(OutputStream output, List<Elem> elems) throws IOException;
    void encodeData(OutputStream output, List<Data> datas) throws IOException;
    void encodeStart(OutputStream output, Integer start) throws IOException;
    void encodeImports(OutputStream output, List<Import> imports) throws IOException;
    void encodeExports(OutputStream output, List<Export> imports) throws IOException;
    void encodeModule(OutputStream output, Module module) throws IOException;
}
