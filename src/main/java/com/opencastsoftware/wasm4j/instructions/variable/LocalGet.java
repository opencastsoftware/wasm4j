package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalGet {
    private final long localIndex;

    public LocalGet(long localIndex) {
        this.localIndex = localIndex;
    }

    public long localIndex() {
        return localIndex;
    }
}
