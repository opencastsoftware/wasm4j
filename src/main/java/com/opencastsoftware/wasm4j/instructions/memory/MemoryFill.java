package com.opencastsoftware.wasm4j.instructions.memory;

public class MemoryFill implements MemoryInstruction {
    private final int memIndex;

    public MemoryFill(int memIndex) {
        this.memIndex = memIndex;
    }

    public MemoryFill() {
        this.memIndex = 0;
    }

    public int memIndex() {
        return memIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitMemoryFill(this);
    }
}
