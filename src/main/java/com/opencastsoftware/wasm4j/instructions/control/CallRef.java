package com.opencastsoftware.wasm4j.instructions.control;

public class CallRef implements ControlInstruction {
    private final int typeIndex;

    public CallRef(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public int typeIndex() {
        return typeIndex;
    }
}
