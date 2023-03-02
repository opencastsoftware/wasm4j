package com.opencastsoftware.wasm4j.types;

public class MemType implements ExternType {
    private final Limits limits;

    public MemType(Limits limits) {
        this.limits = limits;
    }

    public Limits limits() {
        return limits;
    }
}
