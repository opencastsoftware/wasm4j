/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.parametric;

import com.opencastsoftware.wasm4j.types.ValType;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Select implements ParametricInstruction {
    private final List<ValType> valTypes;

    public Select(@Nullable ValType valType) {
        this.valTypes = Optional.ofNullable(valType)
                .map(List::of)
                .orElseGet(Collections::emptyList);
    }

    public Select() {
        this.valTypes = Collections.emptyList();
    }

    public List<ValType> valTypes() {
        return valTypes;
    }

    @Override
    public <T extends Exception> void accept(ParametricInstructionVisitor<T> visitor) throws T {
        visitor.visitSelect(this);
    }
}
