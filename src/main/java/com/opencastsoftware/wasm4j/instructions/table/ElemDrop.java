/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.table;

public class ElemDrop implements TableInstruction {
    private final int elemIndex;

    public ElemDrop(int elemIndex) {
        this.elemIndex = elemIndex;
    }

    public int elemIndex() {
        return elemIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitElemDrop(this);
    }
}
