package com.opencastsoftware.wasm4j.instructions;

public interface Instruction {
    <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T;
}
