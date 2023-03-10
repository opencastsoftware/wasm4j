package com.opencastsoftware.wasm4j.instructions.reference;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.types.HeapType;

public class RefNull implements ReferenceInstruction, ConstantInstruction {
    private HeapType heapType;

    public RefNull(HeapType heapType) {
        this.heapType = heapType;
    }

    public HeapType heapType() {
        return heapType;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitRefNull(this);
    }

    @Override
    public <T extends Exception> void accept(ReferenceInstructionVisitor<T> visitor) throws T {
        visitor.visitRefNull(this);
    }
}
