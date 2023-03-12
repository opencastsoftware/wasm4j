package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.ValType;

import java.util.List;

public class Func {
    private final int typeIndex;
    private final List<ValType> locals;
    private final Expression body;

    public Func(int typeIndex, List<ValType> locals, Expression body) {
        this.typeIndex = typeIndex;
        this.locals = locals;
        this.body = body;
    }

    public int typeIndex() {
        return typeIndex;
    }

    public List<ValType> locals() {
        return locals;
    }

    public Expression body() {
        return body;
    }
}
