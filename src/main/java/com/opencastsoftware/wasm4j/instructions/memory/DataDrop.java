/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public class DataDrop implements MemoryInstruction {
    private final int dataIndex;

    public DataDrop(int dataIndex) {
        this.dataIndex = dataIndex;
    }

    public int dataIndex() {
        return dataIndex;
    }

    @Override
    public <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T {
        visitor.visitDataDrop(this);
    }
}
