/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.parametric;

public enum Drop implements ParametricInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ParametricInstructionVisitor<T> visitor) throws T {
        visitor.visitDrop(this);
    }
}
