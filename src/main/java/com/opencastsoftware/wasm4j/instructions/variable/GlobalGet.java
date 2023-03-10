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

    // FIXME: I don't understand why I have to override this one: there's a default implementation in VariableInstruction
    // It's not because it clashes with the ConstantInstruction implementation, because I don't have to do the same for e.g. I32Const
    @Override
    public <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        visitor.visitGlobalGet(this);
    }
}
