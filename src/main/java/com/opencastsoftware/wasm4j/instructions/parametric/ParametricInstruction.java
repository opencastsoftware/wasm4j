package com.opencastsoftware.wasm4j.instructions.parametric;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.ValType;
import org.jetbrains.annotations.Nullable;

public interface ParametricInstruction extends Instruction {
    static Drop drop() {
        return Drop.INSTANCE;
    }

    static Select select(@Nullable ValType valType) {
        return new Select(valType);
    }

    static Select select() {
        return new Select();
    }
}
