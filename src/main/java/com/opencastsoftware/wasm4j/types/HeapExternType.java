package com.opencastsoftware.wasm4j.types;

public enum HeapExternType implements HeapType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitHeapExtern(this);
    }
}
