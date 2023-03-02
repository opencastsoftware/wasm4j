package com.opencastsoftware.wasm4j.types;

public interface VecType extends ValType {
    static V128Type v128() {
        return V128Type.INSTANCE;
    }
}
