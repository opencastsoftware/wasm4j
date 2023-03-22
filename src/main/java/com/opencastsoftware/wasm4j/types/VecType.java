/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public interface VecType extends ValType {
    static V128Type v128() {
        return V128Type.INSTANCE;
    }
}
