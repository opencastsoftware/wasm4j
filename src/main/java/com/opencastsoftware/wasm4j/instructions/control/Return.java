/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.control;

public enum Return implements ControlInstruction {
    INSTANCE;

    @Override
    public <T extends Exception> void accept(ControlInstructionVisitor<T> visitor) throws T {
        visitor.visitReturn(this);
    }
}
