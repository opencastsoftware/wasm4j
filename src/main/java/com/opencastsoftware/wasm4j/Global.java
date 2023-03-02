package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.types.GlobalType;

import java.util.List;

public class Global {
    private final GlobalType type;
    private final List<ConstantInstruction> init;

    public Global(GlobalType type, List<ConstantInstruction> init) {
        this.type = type;
        this.init = init;
    }

    public GlobalType type() {
        return type;
    }

    public List<ConstantInstruction> init() {
        return init;
    }
}
