package com.opencastsoftware.wasm4j.types;

public enum F32Type implements NumType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitF32Type(this);
    }
}
