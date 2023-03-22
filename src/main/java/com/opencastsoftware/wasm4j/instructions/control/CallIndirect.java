/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.control;

public class CallIndirect implements ControlInstruction {
    private final int tableIndex;
    private final int typeIndex;

    public CallIndirect(int tableIndex, int typeIndex) {
        this.tableIndex = tableIndex;
        this.typeIndex = typeIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    public int typeIndex() {
        return typeIndex;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitCallIndirect(this);
    }
}
