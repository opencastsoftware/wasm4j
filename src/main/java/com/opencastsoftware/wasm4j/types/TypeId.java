package com.opencastsoftware.wasm4j.types;

import com.opencastsoftware.wasm4j.Preconditions;

public class TypeId implements HeapType {
    private final long typeIndex;

    public TypeId(long typeIndex) {
        Preconditions.checkValidU32("typeIndex", typeIndex);
        this.typeIndex = typeIndex;
    }

    public long typeIndex() {
        return typeIndex;
    }
}
