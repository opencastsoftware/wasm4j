package com.opencastsoftware.wasm4j.instructions.numeric.floating;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;

public class F64Const implements NumericInstruction, ConstantInstruction {
    private final double value;

    public F64Const(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }
}
