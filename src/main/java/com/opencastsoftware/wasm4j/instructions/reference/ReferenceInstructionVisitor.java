/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.reference;

public interface ReferenceInstructionVisitor<T extends Exception> {
    default void visitReferenceInstruction(ReferenceInstruction reference) throws T {
        reference.accept(this);
    }

    void visitRefAsNonNull(RefAsNonNull refAsNonNull) throws T;

    void visitRefFunc(RefFunc refFunc) throws T;

    void visitRefIsNull(RefIsNull refIsNull) throws T;

    void visitRefNull(RefNull refNull) throws T;
}
