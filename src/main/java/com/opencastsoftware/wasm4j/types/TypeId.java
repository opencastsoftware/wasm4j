package com.opencastsoftware.wasm4j.types;

public class TypeId implements HeapType, BlockType {
    private final int typeIndex;

    public TypeId(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public int typeIndex() {
        return typeIndex;
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitTypeId(this);
    }
}
