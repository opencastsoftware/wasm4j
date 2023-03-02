package com.opencastsoftware.wasm4j.instructions.reference;

public class RefFunc implements ReferenceInstruction {
    private final long funcIndex;

    public RefFunc(long funcIndex) {
        this.funcIndex = funcIndex;
    }

    public long funcIndex() {
        return funcIndex;
    }
}
