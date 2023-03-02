package com.opencastsoftware.wasm4j.types;

import java.util.List;

public class FuncType implements ExternType {
    private final List<ValType> arguments;
    private final List<ValType> results;

    public FuncType(List<ValType> arguments, List<ValType> results) {
        this.arguments = arguments;
        this.results = results;
    }

    public List<ValType> arguments() {
        return arguments;
    }

    public List<ValType> results() {
        return results;
    }
}
