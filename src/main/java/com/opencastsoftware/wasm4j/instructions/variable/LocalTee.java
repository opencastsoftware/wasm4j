package com.opencastsoftware.wasm4j.instructions.variable;

public class LocalTee {
    private final int localIndex;

    public LocalTee(int localIndex) {
        this.localIndex = localIndex;
    }

    public int localIndex() {
        return localIndex;
    }
}
