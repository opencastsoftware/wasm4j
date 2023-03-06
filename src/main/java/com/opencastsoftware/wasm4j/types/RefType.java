package com.opencastsoftware.wasm4j.types;

public class RefType implements ValType {
    private final boolean nullable;
    private final HeapType heapType;

    public RefType(boolean nullable, HeapType heapType) {
        this.nullable = nullable;
        this.heapType = heapType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public HeapType heapType() {
        return heapType;
    }

    public static RefType nullable(HeapType heapType) {
        return new RefType(true, heapType);
    }

    public static RefType nonNullable(HeapType heapType) {
        return new RefType(false, heapType);
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitRefType(this);
    }
}
