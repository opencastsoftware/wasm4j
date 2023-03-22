/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.reference;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;

public class RefFunc implements ReferenceInstruction, ConstantInstruction {
    private final int funcIndex;

    public RefFunc(int funcIndex) {
        this.funcIndex = funcIndex;
    }

    public int funcIndex() {
        return funcIndex;
    }

    @Override
    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitRefFunc(this);
    }

    @Override
    public <T extends Exception> void accept(ReferenceInstructionVisitor<T> visitor) throws T {
        visitor.visitRefFunc(this);
    }
}
