package com.opencastsoftware.wasm4j.instructions.reference;

public class RefFunc implements ReferenceInstruction {
    private final int funcIndex;

    public RefFunc(int funcIndex) {
        this.funcIndex = funcIndex;
    }

    public int funcIndex() {
        return funcIndex;
    }
}
