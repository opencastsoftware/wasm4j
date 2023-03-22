/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions;

import com.opencastsoftware.wasm4j.Expression;
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

    void visitExpression(Expression expression) throws T;
}
