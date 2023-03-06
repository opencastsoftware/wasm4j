package com.opencastsoftware.wasm4j.types;

public class GlobalType implements ExternType {
    private final boolean mutable;
    private final ValType valType;

    public GlobalType(boolean mutable, ValType valType) {
        this.mutable = mutable;
        this.valType = valType;
    }

    public boolean isMutable() {
        return mutable;
    }

    public ValType valType() {
        return valType;
    }

    public static GlobalType mutable(ValType valType) {
        return new GlobalType(true, valType);
    }

    public static GlobalType immutable(ValType valType) {
        return new GlobalType(false, valType);
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitGlobalType(this);
    }
}
