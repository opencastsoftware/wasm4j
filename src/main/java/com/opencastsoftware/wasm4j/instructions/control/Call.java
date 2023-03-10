package com.opencastsoftware.wasm4j.instructions.control;

public class Call implements ControlInstruction {
    private final int funcIndex;

    public Call(int funcIndex) {
        this.funcIndex = funcIndex;
    }

    public int funcIndex() {
        return funcIndex;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitCall(this);
    }
}
