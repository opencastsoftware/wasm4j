package com.opencastsoftware.wasm4j.instructions.numeric.integer;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public class I32Const implements NumericInstruction, ConstantInstruction {
    private final int value;

    public I32Const(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitI32Const(this);
    }

    @Override
    public <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T {
        visitor.visitI32Const(this);
    }
}
