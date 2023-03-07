package com.opencastsoftware.wasm4j.instructions.control;

public class Branch implements ControlInstruction {
    private final int labelIndex;

    public Branch(int labelIndex) {
        this.labelIndex = labelIndex;
    }

    public int labelIndex() {
        return labelIndex;
    }
}
