/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.variable;

public interface VariableInstructionVisitor<T extends Exception> {
    default void visitVariableInstruction(VariableInstruction variable) throws T {
        variable.accept(this);
    }

    void visitGlobalGet(GlobalGet globalGet) throws T;

    void visitGlobalSet(GlobalSet globalSet) throws T;

    void visitLocalGet(LocalGet localGet) throws T;

    void visitLocalSet(LocalSet localSet) throws T;

    void visitLocalTee(LocalTee localTee) throws T;
}
