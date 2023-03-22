/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.control;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.BlockType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class If implements ControlInstruction {
    @Nullable
    private final BlockType blockType;
    private final List<Instruction> consequent;
    private final List<Instruction> alternative;

    public If(@Nullable BlockType blockType, List<Instruction> consequent, List<Instruction> alternative) {
        this.blockType = blockType;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Nullable
    public BlockType blockType() {
        return blockType;
    }

    public List<Instruction> consequent() {
        return consequent;
    }

    public List<Instruction> alternative() {
        return alternative;
    }

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitIf(this);
    }
}
