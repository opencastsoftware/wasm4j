package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalTee implements VariableInstruction {
    private final int localIndex;

    public LocalTee(int localIndex) {
        this.localIndex = localIndex;
    }

    public int localIndex() {
        return localIndex;
    }

    @Override
    public <T extends Exception> void accept(VariableInstructionVisitor<T> visitor) throws T {
        visitor.visitLocalTee(this);
    }
}
