package com.opencastsoftware.wasm4j.instructions.reference;

public enum RefIsNull implements ReferenceInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ReferenceInstructionVisitor<T> visitor) throws T {
        visitor.visitRefIsNull(this);
    }
}
