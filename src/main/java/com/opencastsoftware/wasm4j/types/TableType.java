package com.opencastsoftware.wasm4j.types;

public class TableType implements ExternType {
    private final Limits limits;
    private final RefType refType;

    public TableType(Limits limits, RefType refType) {
        this.limits = limits;
        this.refType = refType;
    }

    public Limits limits() {
        return limits;
    }

    public RefType refType() {
        return refType;
    }
}
