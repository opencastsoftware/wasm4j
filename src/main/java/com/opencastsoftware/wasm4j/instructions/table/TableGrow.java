package com.opencastsoftware.wasm4j.instructions.table;

public class TableGrow implements TableInstruction {
    private final int tableIndex;

    public TableGrow(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableGrow(this);
    }
}
