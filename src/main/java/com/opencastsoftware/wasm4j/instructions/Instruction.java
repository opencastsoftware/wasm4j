/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions;

public interface Instruction {
    <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T;
}
