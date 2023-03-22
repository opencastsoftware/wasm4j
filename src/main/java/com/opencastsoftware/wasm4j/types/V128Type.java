/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public enum V128Type implements VecType {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitV128Type(this);
    }
}
