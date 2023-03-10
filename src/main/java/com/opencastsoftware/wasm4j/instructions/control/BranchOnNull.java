package com.opencastsoftware.wasm4j.instructions.control;

public class BranchOnNull implements ControlInstruction {
    private final int labelIndex;

    public BranchOnNull(int labelIndex) {
        this.labelIndex = labelIndex;
    }

    public int labelIndex() {
        return labelIndex;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitBranchOnNull(this);
    }
}
