package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalSet {
    private final int localIndex;

    public LocalSet(int localIndex) {
        this.localIndex = localIndex;
    }

    public int localIndex() {
        return localIndex;
    }
}
