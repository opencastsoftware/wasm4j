package com.opencastsoftware.wasm4j.instructions.table;

public class TableSize implements TableInstruction {
    private final int tableIndex;

    public TableSize(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableSize(this);
    }
}
