/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.GlobalType;

public class Global {
    private final GlobalType type;
    private final ConstantExpression init;

    public Global(GlobalType type, ConstantExpression init) {
        this.type = type;
        this.init = init;
    }

    public GlobalType type() {
        return type;
    }

    public ConstantExpression init() {
        return init;
    }
}
