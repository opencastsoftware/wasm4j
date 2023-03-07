package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.Instruction;

import java.util.List;

public abstract class BaseExpression<T extends Instruction> {
    protected final List<T> instructions;

    public BaseExpression(List<T> instructions) {
        this.instructions = instructions;
    }

    public List<T> instructions() {
        return instructions;
    }
}
