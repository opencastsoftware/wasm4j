package com.opencastsoftware.wasm4j.instructions.parametric;

import com.opencastsoftware.wasm4j.types.ValType;

public class Select implements ParametricInstruction {
    private ValType valType;

    public Select(ValType valType) {
        this.valType = valType;
    }

    public Select() {
        this.valType = null;
    }
}
