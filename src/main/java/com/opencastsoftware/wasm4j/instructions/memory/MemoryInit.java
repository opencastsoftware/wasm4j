/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public class MemoryInit implements MemoryInstruction {
    private final int memIndex;
    private final int dataIndex;

    public MemoryInit(int memIndex, int dataIndex) {
        this.memIndex = memIndex;
        this.dataIndex = dataIndex;
    }

    public MemoryInit(int dataIndex) {
        this.memIndex = 0;
        this.dataIndex = dataIndex;
    }

    public int memIndex() {
        return memIndex;
    }

    public int dataIndex() {
        return dataIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitMemoryInit(this);
    }
}
