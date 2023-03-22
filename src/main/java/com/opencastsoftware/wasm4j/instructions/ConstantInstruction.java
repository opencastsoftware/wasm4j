/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions;

public interface ConstantInstruction extends Instruction {
    <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T;
}
