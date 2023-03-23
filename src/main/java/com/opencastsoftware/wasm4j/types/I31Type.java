package com.opencastsoftware.wasm4j.types;

public enum I31Type implements HeapType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitI31Type(this);
    }
}
