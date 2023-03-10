package com.opencastsoftware.wasm4j.instructions.memory;

public class F32Load implements MemoryInstruction {
    private final int offset;
    private final int align;

    public F32Load(int offset, int align) {
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
        visitor.visitF32Load(this);
    }
}
