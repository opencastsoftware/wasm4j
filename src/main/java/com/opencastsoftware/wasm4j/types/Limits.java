/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

import org.jetbrains.annotations.Nullable;

public class Limits implements WasmType {
    private final int min;

    @Nullable
    private final Integer max;

    public Limits(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public Limits(int min) {
        this.min = min;
        this.max = null;
    }

    public int min() {
        return min;
    }

    @Nullable
    public Integer max() {
        return max;
    }

    public static Limits of(int min) {
        return new Limits(min);
    }

    public static Limits of(int min, int max) {
        return new Limits(min, max);
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitLimits(this);
    }
}
