package com.opencastsoftware.wasm4j.types;

public interface NumType extends ValType {
    static I32Type i32() {
        return I32Type.INSTANCE;
    }

    static I64Type i64() {
        return I64Type.INSTANCE;
    }

    static F32Type f32() {
        return F32Type.INSTANCE;
    }

    static F64Type f64() {
        return F64Type.INSTANCE;
    }
}
