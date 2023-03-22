/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.numeric.floating.unary;

import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public enum F64Floor implements NumericInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T {
        visitor.visitF64Floor(this);
    }
}
