package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalGet implements VariableInstruction {
    private final int localIndex;

    public LocalGet(int localIndex) {
        this.localIndex = localIndex;
    }

    public int localIndex() {
        return localIndex;
    }

    @Override
    public <T extends Exception> void accept(VariableInstructionVisitor<T> visitor) throws T {
        visitor.visitLocalGet(this);
    }
}
