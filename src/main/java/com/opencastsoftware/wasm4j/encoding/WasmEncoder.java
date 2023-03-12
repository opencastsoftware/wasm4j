package com.opencastsoftware.wasm4j.encoding;

import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;

import java.io.OutputStream;
import java.util.List;

public interface WasmEncoder<T extends Exception> {
    void encodeTypes(OutputStream output, List<FuncType> types) throws T;
    void encodeFunctions(OutputStream output, List<Func> funcs) throws T;
    void encodeTables(OutputStream output, List<Table> tables) throws T;
    void encodeMemories(OutputStream output, List<MemType> mems) throws T;
    void encodeGlobals(OutputStream output, List<Global> globals) throws T;
    void encodeElems(OutputStream output, List<Elem> elems) throws T;
    void encodeData(OutputStream output, List<Data> datas) throws T;
    void encodeStart(OutputStream output, Integer start) throws T;
    void encodeImports(OutputStream output, List<Import> imports) throws T;
    void encodeExports(OutputStream output, List<Export> imports) throws T;
    void encodeModule(OutputStream output, Module module) throws T;
}
