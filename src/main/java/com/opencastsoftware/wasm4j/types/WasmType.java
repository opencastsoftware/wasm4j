/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public interface WasmType {
    <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T;
}
