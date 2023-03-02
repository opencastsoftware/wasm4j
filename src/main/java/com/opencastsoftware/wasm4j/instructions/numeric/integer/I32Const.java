package com.opencastsoftware.wasm4j.instructions.numeric.integer;

import com.opencastsoftware.wasm4j.Preconditions;
import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;

public class I32Const implements NumericInstruction, ConstantInstruction {
    private final long value;

    public I32Const(long value) {
        Preconditions.checkValidU32("value", value);
        this.value = value;
    }

    public long value() {
        return value;
    }
}
