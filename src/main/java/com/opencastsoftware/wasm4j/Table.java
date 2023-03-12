package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.TableType;

public class Table {
    private final TableType type;
    private final ConstantExpression init;

    public Table(TableType type, ConstantExpression init) {
        this.type = type;
        this.init = init;
    }

    public TableType type() {
        return type;
    }

    public ConstantExpression init() {
        return init;
    }
}
