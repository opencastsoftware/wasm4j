package com.opencastsoftware.wasm4j.instructions.memory;

public class I32Store implements MemoryInstruction {
    private final int offset;
    private final int align;

    public I32Store(int offset, int align) {
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
        visitor.visitI32Store(this);
    }
}
