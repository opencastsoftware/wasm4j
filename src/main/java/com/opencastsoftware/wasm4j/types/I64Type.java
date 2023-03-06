package com.opencastsoftware.wasm4j.types;

public enum I64Type implements NumType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitI64Type(this);
    }
}
