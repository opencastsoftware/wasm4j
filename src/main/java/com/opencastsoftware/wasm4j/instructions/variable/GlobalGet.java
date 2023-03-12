package com.opencastsoftware.wasm4j.instructions.variable;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;

public class GlobalGet implements VariableInstruction, ConstantInstruction {
    private final int globalIndex;

    public GlobalGet(int globalIndex) {
        this.globalIndex = globalIndex;
    }

    public int globalIndex() {
        return globalIndex;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitGlobalGet(this);
    }

    @Override
    public <T extends Exception> void accept(VariableInstructionVisitor<T> visitor) throws T {
        visitor.visitGlobalGet(this);
    }
}
