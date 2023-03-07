package com.opencastsoftware.wasm4j.instructions.numeric.integer;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;

public class I64Const implements NumericInstruction, ConstantInstruction {
    private final long value;

    public I64Const(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitI64Const(this);
    }
}
