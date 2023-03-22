/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.table;

public class TableCopy implements TableInstruction {
    private final int targetTableIndex;
    private final int sourceTableIndex;

    public TableCopy(int targetTableIndex, int sourceTableIndex) {
        this.targetTableIndex = targetTableIndex;
        this.sourceTableIndex = sourceTableIndex;
    }

    public int targetTableIndex() {
        return targetTableIndex;
    }

    public int sourceTableIndex() {
        return sourceTableIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableCopy(this);
    }
}
