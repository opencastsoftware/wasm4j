package com.opencastsoftware.wasm4j.types;

public enum F64Type implements NumType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitF64Type(this);
    }
}
