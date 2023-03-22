/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
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

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitFuncType(this);
    }
}
