package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.ValType;

import java.util.List;

public class Func {
    private final int typeIndex;
    private final List<ValType> locals;
    private final List<Instruction> body;

    public Func(int typeIndex, List<ValType> locals, List<Instruction> body) {
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

    public List<Instruction> body() {
        return body;
    }
}
