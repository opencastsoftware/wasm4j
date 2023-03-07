package com.opencastsoftware.wasm4j.instructions.control;

public class CallIndirect implements ControlInstruction {
    private final int tableIndex;
    private final int typeIndex;

    public CallIndirect(int tableIndex, int typeIndex) {
        this.tableIndex = tableIndex;
        this.typeIndex = typeIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    public int typeIndex() {
        return typeIndex;
    }
}
