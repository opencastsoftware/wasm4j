package com.opencastsoftware.wasm4j.instructions.variable;

public interface VariableInstruction {
    static LocalGet local_get(int localIndex) {
        return new LocalGet(localIndex);
    }

    static LocalSet local_set(int localIndex) {
        return new LocalSet(localIndex);
    }

    static LocalTee local_tee(int localIndex) {
        return new LocalTee(localIndex);
    }

    static GlobalGet global_get(int globalIndex) {
        return new GlobalGet(globalIndex);
    }

    static GlobalSet global_set(int globalIndex) {
        return new GlobalSet(globalIndex);
    }
}
