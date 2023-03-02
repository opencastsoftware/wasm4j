package com.opencastsoftware.wasm4j.instructions.numeric.integer;

import com.opencastsoftware.wasm4j.Preconditions;
import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;

import java.math.BigInteger;

public class I64Const implements NumericInstruction, ConstantInstruction {
    private final BigInteger value;

    public I64Const(BigInteger value) {
        Preconditions.checkValidU64("value", value);
        this.value = value;
    }

    public BigInteger value() {
        return value;
    }
}
