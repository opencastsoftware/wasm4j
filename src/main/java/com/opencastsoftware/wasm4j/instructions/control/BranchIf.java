package com.opencastsoftware.wasm4j.instructions.control;

public class BranchIf implements ControlInstruction {
    private final int labelIndex;

    public BranchIf(int labelIndex) {
        this.labelIndex = labelIndex;
    }

    public int labelIndex() {
        return labelIndex;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitBranchIf(this);
    }
}
