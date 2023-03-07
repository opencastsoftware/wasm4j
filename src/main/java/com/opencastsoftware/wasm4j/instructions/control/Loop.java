package com.opencastsoftware.wasm4j.instructions.control;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.BlockType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Loop implements ControlInstruction {
    @Nullable
    private final BlockType blockType;
    private final List<Instruction> instructions;

    public Loop(@Nullable BlockType blockType, List<Instruction> instructions) {
        this.blockType = blockType;
        this.instructions = instructions;
    }

    @Nullable
    public BlockType blockType() {
        return blockType;
    }

    public List<Instruction> instructions() {
        return instructions;
    }
}
