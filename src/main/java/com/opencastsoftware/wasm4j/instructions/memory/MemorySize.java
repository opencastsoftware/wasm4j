/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public class MemorySize implements MemoryInstruction {
    private final int memIndex;

    public MemorySize(int memIndex) {
        this.memIndex = memIndex;
    }

    public MemorySize() {
        this.memIndex = 0;
    }

    public int memIndex() {
        return memIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitMemorySize(this);
    }
}
