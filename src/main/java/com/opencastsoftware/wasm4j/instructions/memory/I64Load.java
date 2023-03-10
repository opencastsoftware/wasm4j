package com.opencastsoftware.wasm4j.instructions.memory;

public class I64Load implements MemArgInstruction {
    private final int offset;
    private final int align;

    public I64Load(int offset, int align) {
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
        visitor.visitI64Load(this);
    }
}
