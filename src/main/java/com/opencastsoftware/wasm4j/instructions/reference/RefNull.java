package com.opencastsoftware.wasm4j.instructions.reference;

import com.opencastsoftware.wasm4j.types.HeapType;

public class RefNull implements ReferenceInstruction {
    private HeapType heapType;

    public RefNull(HeapType heapType) {
        this.heapType = heapType;
    }

    public HeapType heapType() {
        return heapType;
    }
}
