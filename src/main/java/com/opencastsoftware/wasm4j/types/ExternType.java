package com.opencastsoftware.wasm4j.types;

import java.util.List;

public interface ExternType extends WasmType {
    static FuncType func(List<ValType> arguments, List<ValType> results) {
        return new FuncType(arguments, results);
    }

    static TableType table(Limits limits, RefType refType) {
        return new TableType(limits, refType);
    }

    static MemType mem(Limits limits) {
        return new MemType(limits);
    }

    static GlobalType global(boolean mutable, ValType valType) {
        return new GlobalType(mutable, valType);
    }
}
