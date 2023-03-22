/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public class MemoryCopy implements MemoryInstruction {
    private final int targetMemIndex;
    private final int sourceMemIndex;

    public MemoryCopy(int targetMemIndex, int sourceMemIndex) {
        this.targetMemIndex = targetMemIndex;
        this.sourceMemIndex = sourceMemIndex;
    }

    public MemoryCopy() {
        this.targetMemIndex = 0;
        this.sourceMemIndex = 0;
    }

    public int targetMemIndex() {
        return targetMemIndex;
    }

    public int sourceMemIndex() {
        return sourceMemIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitMemoryCopy(this);
    }
}
