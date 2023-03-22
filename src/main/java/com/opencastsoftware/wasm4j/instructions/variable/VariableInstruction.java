/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.variable;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;

public interface VariableInstruction extends Instruction {
    <T extends Exception> void accept(VariableInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((VariableInstructionVisitor<T>) visitor);
    }

    static LocalGet local_get(int localIndex) {
        return new LocalGet(localIndex);
    }

    static LocalSet local_set(int localIndex) {
        return new LocalSet(localIndex);
    }

    static LocalTee local_tee(int localIndex) {
        return new LocalTee(localIndex);
    }

    static GlobalGet global_get(int globalIndex) {
        return new GlobalGet(globalIndex);
    }

    static GlobalSet global_set(int globalIndex) {
        return new GlobalSet(globalIndex);
    }
}
