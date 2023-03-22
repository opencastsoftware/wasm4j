/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;

public interface MemoryInstruction extends Instruction {
    <T extends Exception> void accept(MemoryInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((MemoryInstructionVisitor<T>) visitor);
    }

    static I32Load i32_load(int offset, int align) {
        return new I32Load(offset, align);
    }

    static I64Load i64_load(int offset, int align) {
        return new I64Load(offset, align);
    }

    static F32Load f32_load(int offset, int align) {
        return new F32Load(offset, align);
    }

    static F64Load f64_load(int offset, int align) {
        return new F64Load(offset, align);
    }

    static I32Store i32_store(int offset, int align) {
        return new I32Store(offset, align);
    }

    static I64Store i64_store(int offset, int align) {
        return new I64Store(offset, align);
    }

    static F32Store f32_store(int offset, int align) {
        return new F32Store(offset, align);
    }

    static F64Store f64_store(int offset, int align) {
        return new F64Store(offset, align);
    }

    static MemorySize memory_size() {
        return new MemorySize();
    }

    static MemoryGrow memory_grow() {
        return new MemoryGrow();
    }

    static MemoryFill memory_fill() {
        return new MemoryFill();
    }

    static MemoryCopy memory_copy() {
        return new MemoryCopy();
    }

    static MemoryInit memory_init(int dataIndex) {
        return new MemoryInit(dataIndex);
    }

    static DataDrop data_drop(int dataIndex) {
        return new DataDrop(dataIndex);
    }
}
