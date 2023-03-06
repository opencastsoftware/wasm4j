package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalGet {
    private final int localIndex;

    public LocalGet(int localIndex) {
        this.localIndex = localIndex;
    }

    public int localIndex() {
        return localIndex;
    }
}
