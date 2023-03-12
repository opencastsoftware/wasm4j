package com.opencastsoftware.wasm4j.instructions.table;

public class TableGet implements TableInstruction {
    private final int tableIndex;

    public TableGet(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public int tableIndex() {
        return tableIndex;
    }

    @Override
    public <T extends Exception> void accept(TableInstructionVisitor<T> visitor) throws T {
        visitor.visitTableGet(this);
    }
}
