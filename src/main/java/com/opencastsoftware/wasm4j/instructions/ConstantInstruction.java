package com.opencastsoftware.wasm4j.instructions;

public interface ConstantInstruction extends Instruction {
    <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T;
}
