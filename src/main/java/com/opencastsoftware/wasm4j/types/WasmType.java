package com.opencastsoftware.wasm4j.types;

public interface WasmType {
    <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T;
}
