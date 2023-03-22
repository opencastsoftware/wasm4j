/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.numeric.integer.conversion;

import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public enum I32WrapI64 implements NumericInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T {
        visitor.visitI32WrapI64(this);
    }
}
