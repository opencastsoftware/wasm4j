package com.opencastsoftware.wasm4j.instructions.control;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;
import com.opencastsoftware.wasm4j.types.BlockType;

import java.util.List;

public interface ControlInstruction extends Instruction {
    <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((ControlInstructionVisitor<T>) visitor);
    }

    static Nop nop() {
        return Nop.INSTANCE;
    }

    static Unreachable unreachable() {
        return Unreachable.INSTANCE;
    }

    static Block block(BlockType blockType, List<Instruction> instructions) {
        return new Block(blockType, instructions);
    }

    static Block block(BlockType blockType, Instruction... instructions) {
        return block(blockType, List.of(instructions));
    }

    static Block block(List<Instruction> instructions) {
        return new Block(null, instructions);
    }

    static Block block(Instruction... instructions) {
        return block(List.of(instructions));
    }

    static Loop loop(BlockType blockType, List<Instruction> instructions) {
        return new Loop(blockType, instructions);
    }

    static Loop loop(BlockType blockType, Instruction... instructions) {
        return loop(blockType, List.of(instructions));
    }

    static Loop loop(List<Instruction> instructions) {
        return new Loop(null, instructions);
    }

    static Loop loop(Instruction... instructions) {
        return loop(List.of(instructions));
    }

    static If ifInstr(BlockType blockType, List<Instruction> consequent, List<Instruction> alternative) {
        return new If(blockType, consequent, alternative);
    }

    static If ifInstr(List<Instruction> consequent, List<Instruction> alternative) {
        return new If(null, consequent, alternative);
    }

    static Branch br(int labelIndex) {
        return new Branch(labelIndex);
    }

    static BranchIf br_if(int labelIndex) {
        return new BranchIf(labelIndex);
    }

    static BranchTable br_table(int[] labels, int defaultLabel) {
        return new BranchTable(labels, defaultLabel);
    }

    static BranchOnNull br_on_null(int labelIndex) {
        return new BranchOnNull(labelIndex);
    }

    static BranchOnNonNull br_on_non_null(int labelIndex) {
        return new BranchOnNonNull(labelIndex);
    }

    static Return ret() {
        return Return.INSTANCE;
    }

    static Call call(int funcIndex) {
        return new Call(funcIndex);
    }

    static CallRef call_ref(int typeIndex) {
        return new CallRef(typeIndex);
    }

    static CallIndirect call_indirect(int tableIndex, int typeIndex) {
        return new CallIndirect(tableIndex, typeIndex);
    }
}
