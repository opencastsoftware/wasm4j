package com.opencastsoftware.wasm4j.instructions.variable;

public class GlobalSet {
    private final int globalIndex;

    public GlobalSet(int globalIndex) {
        this.globalIndex = globalIndex;
    }

    public int globalIndex() {
        return globalIndex;
    }
}
