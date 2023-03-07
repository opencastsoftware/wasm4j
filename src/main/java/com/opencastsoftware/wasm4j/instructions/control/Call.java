package com.opencastsoftware.wasm4j.instructions.control;

public class Call implements ControlInstruction {
    private final int funcIndex;

    public Call(int funcIndex) {
        this.funcIndex = funcIndex;
    }

    public int funcIndex() {
        return funcIndex;
    }
}
