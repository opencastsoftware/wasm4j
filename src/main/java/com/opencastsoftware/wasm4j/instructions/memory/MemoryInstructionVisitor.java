/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.memory;

public interface MemoryInstructionVisitor<T extends Exception> {
    default void visitMemoryInstruction(MemoryInstruction memory) throws T {
        memory.accept(this);
    }

    void visitI32Load(I32Load i32Load) throws T;

    void visitI64Load(I64Load i64Load) throws T;

    void visitF32Load(F32Load f32Load) throws T;

    void visitF64Load(F64Load f64Load) throws T;

    void visitI32Store(I32Store i32Store) throws T;

    void visitI64Store(I64Store i64Store) throws T;

    void visitF32Store(F32Store f32Store) throws T;

    void visitF64Store(F64Store f64Store) throws T;

    void visitMemorySize(MemorySize memorySize) throws T;

    void visitMemoryGrow(MemoryGrow memoryGrow) throws T;

    void visitMemoryFill(MemoryFill memoryFill) throws T;

    void visitMemoryCopy(MemoryCopy memoryCopy) throws T;

    void visitMemoryInit(MemoryInit memoryInit) throws T;

    void visitDataDrop(DataDrop dataDrop) throws T;
}
