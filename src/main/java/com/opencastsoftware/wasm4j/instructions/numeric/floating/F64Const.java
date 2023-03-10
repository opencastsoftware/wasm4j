package com.opencastsoftware.wasm4j.instructions.numeric.floating;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public class F64Const implements NumericInstruction, ConstantInstruction {
    private final double value;

    public F64Const(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitF64Const(this);
    }

    @Override
    public <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T {
        visitor.visitF64Const(this);
    }
}
