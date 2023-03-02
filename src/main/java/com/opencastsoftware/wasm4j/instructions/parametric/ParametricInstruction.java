package com.opencastsoftware.wasm4j.instructions.parametric;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.ValType;

public interface ParametricInstruction extends Instruction {
    static Drop drop() {
        return Drop.INSTANCE;
    }

    static Select select(ValType valType) {
        return new Select(valType);
    }

    static Select select() {
        return new Select();
    }
}
