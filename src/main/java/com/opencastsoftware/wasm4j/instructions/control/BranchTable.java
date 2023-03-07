package com.opencastsoftware.wasm4j.instructions.control;

public class BranchTable implements ControlInstruction {
    private final int[] labels;
    private final int defaultLabel;

    public BranchTable(int[] labels, int defaultLabel) {
        this.labels = labels;
        this.defaultLabel = defaultLabel;
    }

    public int[] labels() {
        return labels;
    }

    public int defaultLabel() {
        return defaultLabel;
    }
}
