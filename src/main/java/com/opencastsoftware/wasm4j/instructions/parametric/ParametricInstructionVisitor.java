/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.parametric;

public interface ParametricInstructionVisitor<T extends Exception> {
    default void visitParametricInstruction(ParametricInstruction parametric) throws T {
        parametric.accept(this);
    }

    void visitDrop(Drop drop) throws T;

    void visitSelect(Select select) throws T;
}
