package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.types.TableType;

import java.util.List;

public class Table {
    private final TableType type;
    private final List<ConstantInstruction> init;

    public Table(TableType type, List<ConstantInstruction> init) {
        this.type = type;
        this.init = init;
    }

    public TableType type() {
        return type;
    }

    public List<ConstantInstruction> init() {
        return init;
    }
}
