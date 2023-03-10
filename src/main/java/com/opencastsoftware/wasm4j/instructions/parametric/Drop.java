package com.opencastsoftware.wasm4j.instructions.parametric;

public enum Drop implements ParametricInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ParametricInstructionVisitor<T> visitor) throws T {
        visitor.visitDrop(this);
    }
}
