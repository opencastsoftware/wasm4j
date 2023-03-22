/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.numeric.floating;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstructionVisitor;

public class F32Const implements NumericInstruction, ConstantInstruction {
    private final float value;

    public F32Const(float value) {
        this.value = value;
    }

    public float value() {
        return value;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitF32Const(this);
    }

    @Override
    public <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T {
        visitor.visitF32Const(this);
    }
}
