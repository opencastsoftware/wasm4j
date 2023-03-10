package com.opencastsoftware.wasm4j.instructions.table;

import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;

public interface TableInstruction {
    <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((TableInstructionVisitor<T>) visitor);
    }

    static TableGet table_get(int tableIndex) {
        return new TableGet(tableIndex);
    }

    static TableSet table_set(int tableIndex) {
        return new TableSet(tableIndex);
    }

    static TableSize table_size(int tableIndex) {
        return new TableSize(tableIndex);
    }

    static TableGrow table_grow(int tableIndex) {
        return new TableGrow(tableIndex);
    }

    static TableFill table_fill(int tableIndex) {
        return new TableFill(tableIndex);
    }

    static TableCopy table_copy(int targetTableIndex, int sourceTableIndex) {
        return new TableCopy(targetTableIndex, sourceTableIndex);
    }

    static TableInit table_init(int tableIndex, int elemIndex) {
        return new TableInit(tableIndex, elemIndex);
    }

    static ElemDrop elem_drop(int elemIndex) {
        return new ElemDrop(elemIndex);
    }
}
