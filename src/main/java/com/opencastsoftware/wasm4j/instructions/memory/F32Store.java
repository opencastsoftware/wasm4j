package com.opencastsoftware.wasm4j.instructions.memory;

public class F32Store implements MemArgInstruction {
    private final int offset;
    private final int align;

    public F32Store(int offset, int align) {
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
        visitor.visitF32Store(this);
    }
}
