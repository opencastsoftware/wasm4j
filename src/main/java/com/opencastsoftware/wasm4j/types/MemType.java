/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public class MemType implements ExternType {
    private final Limits limits;

    public MemType(Limits limits) {
        this.limits = limits;
    }

    public Limits limits() {
        return limits;
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitMemType(this);
    }
}
