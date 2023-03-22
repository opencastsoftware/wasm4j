/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public class TableType implements ExternType {
    private final Limits limits;
    private final RefType refType;

    public TableType(Limits limits, RefType refType) {
        this.limits = limits;
        this.refType = refType;
    }

    public Limits limits() {
        return limits;
    }

    public RefType refType() {
        return refType;
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitTableType(this);
    }
}
