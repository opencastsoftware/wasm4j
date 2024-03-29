/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.control;

public class CallRef implements ControlInstruction {
    private final int typeIndex;

    public CallRef(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public int typeIndex() {
        return typeIndex;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitCallRef(this);
    }
}
