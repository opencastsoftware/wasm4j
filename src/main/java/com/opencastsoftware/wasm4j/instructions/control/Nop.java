package com.opencastsoftware.wasm4j.instructions.control;

public enum Nop implements ControlInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitNop(this);
    }
}
