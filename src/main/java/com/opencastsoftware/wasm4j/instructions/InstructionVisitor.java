package com.opencastsoftware.wasm4j.instructions;

import com.opencastsoftware.wasm4j.instructions.control.ControlInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.memory.MemoryInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.parametric.ParametricInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.reference.ReferenceInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.table.TableInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.variable.VariableInstructionVisitor;

public interface InstructionVisitor<T extends Exception> extends ControlInstructionVisitor<T>, MemoryInstructionVisitor<T>, NumericInstructionVisitor<T>, ParametricInstructionVisitor<T>, ReferenceInstructionVisitor<T>, TableInstructionVisitor<T>, VariableInstructionVisitor<T>, ConstantInstructionVisitor<T> {
    default void visitInstruction(Instruction instruction) throws T {
        instruction.accept(this);
    }
}
