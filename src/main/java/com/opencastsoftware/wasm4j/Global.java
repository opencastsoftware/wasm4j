package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.types.GlobalType;

import java.util.List;

public class Global {
    private final GlobalType type;
    private final ConstantExpression init;

    public Global(GlobalType type, ConstantExpression init) {
        this.type = type;
        this.init = init;
    }

    public GlobalType type() {
        return type;
    }

    public ConstantExpression init() {
        return init;
    }
}
