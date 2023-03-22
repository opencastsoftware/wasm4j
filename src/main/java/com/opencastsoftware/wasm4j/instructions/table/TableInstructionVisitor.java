/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.table;

public interface TableInstructionVisitor<T extends Exception> {
    default void visitTableInstruction(TableInstruction table) throws T {
        table.accept(this);
    }

    void visitTableGet(TableGet tableGet) throws T;

    void visitTableSet(TableSet tableSet) throws T;

    void visitTableSize(TableSize tableSize) throws T;

    void visitTableGrow(TableGrow tableGrow) throws T;

    void visitTableFill(TableFill tableFill) throws T;

    void visitTableCopy(TableCopy tableCopy) throws T;

    void visitTableInit(TableInit tableInit) throws T;

    void visitElemDrop(ElemDrop elemDrop) throws T;
}
