package com.opencastsoftware.wasm4j.instructions.table;

public class TableInit implements TableInstruction {
    private final int tableIndex;
    private final int elemIndex;

    public TableInit(int tableIndex, int elemIndex) {
        this.tableIndex = tableIndex;
        this.elemIndex = elemIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    public int elemIndex() {
        return elemIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableInit(this);
    }
}
