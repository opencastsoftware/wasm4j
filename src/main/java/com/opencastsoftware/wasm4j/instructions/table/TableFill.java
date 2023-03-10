package com.opencastsoftware.wasm4j.instructions.table;

public class TableFill implements TableInstruction {
    private final int tableIndex;

    public TableFill(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableFill(this);
    }
}
