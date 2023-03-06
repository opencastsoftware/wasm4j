package com.opencastsoftware.wasm4j.instructions.numeric.integer;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;

public class I32Const implements NumericInstruction, ConstantInstruction {
    private final int value;

    public I32Const(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
