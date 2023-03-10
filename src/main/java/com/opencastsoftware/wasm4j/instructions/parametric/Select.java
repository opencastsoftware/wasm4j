package com.opencastsoftware.wasm4j.instructions.parametric;

import com.opencastsoftware.wasm4j.types.ValType;
import org.jetbrains.annotations.Nullable;

public class Select implements ParametricInstruction {
    @Nullable
    private final ValType valType;

    public Select(@Nullable ValType valType) {
        this.valType = valType;
    }

    public Select() {
        this.valType = null;
    }

    @Nullable
    public ValType valType() {
        return valType;
    }

    @Override
    public <T extends Exception> void accept(ParametricInstructionVisitor<T> visitor) throws T {
        visitor.visitSelect(this);
    }
}
