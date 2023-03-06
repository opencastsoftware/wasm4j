package com.opencastsoftware.wasm4j.types;

public interface HeapType extends WasmType {
    static HeapFuncType func() {
        return HeapFuncType.INSTANCE;
    }

    static HeapExternType extern() {
        return HeapExternType.INSTANCE;
    }

    static TypeId typeId(int typeIndex) {
        return new TypeId(typeIndex);
    }
}
