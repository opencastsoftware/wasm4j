package com.opencastsoftware.wasm4j.instructions.variable;

public class GlobalSet implements VariableInstruction {
    private final int globalIndex;

    public GlobalSet(int globalIndex) {
        this.globalIndex = globalIndex;
    }

    public int globalIndex() {
        return globalIndex;
    }

    @Override
    public <T extends Exception> void accept(VariableInstructionVisitor<T> visitor) throws T {
        visitor.visitGlobalSet(this);
    }
}
