package com.opencastsoftware.wasm4j.instructions;

public interface InstructionVisitor<T extends Exception> extends ConstantInstructionVisitor<T> {
    void visitInstruction(Instruction instruction) throws T;
}
