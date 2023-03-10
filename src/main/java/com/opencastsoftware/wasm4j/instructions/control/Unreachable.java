package com.opencastsoftware.wasm4j.instructions.control;

public enum Unreachable implements ControlInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitUnreachable(this);
    }
}
