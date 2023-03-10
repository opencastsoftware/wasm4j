package com.opencastsoftware.wasm4j.instructions.memory;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public class I32Load implements MemoryInstruction {
    private final int offset;
    private final int align;

    public I32Load(int offset, int align) {
        this.offset = offset;
        this.align = align;
    }

    public int offset() {
        return offset;
    }

    public int align() {
        return align;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitI32Load(this);
    }
}
