/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public class MemoryGrow implements MemoryInstruction {
    private final int memIndex;

    public MemoryGrow(int memIndex) {
        this.memIndex = memIndex;
    }

    public MemoryGrow() {
        this.memIndex = 0;
    }

    public int memIndex() {
        return memIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitMemoryGrow(this);
    }
}
