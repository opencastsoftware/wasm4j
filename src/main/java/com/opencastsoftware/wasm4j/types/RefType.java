/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;

public class RefType implements ValType {
    private final boolean nullable;
    private final HeapType heapType;

    public RefType(boolean nullable, HeapType heapType) {
        this.nullable = nullable;
        this.heapType = heapType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public HeapType heapType() {
        return heapType;
    }

    public static RefType nullable(HeapType heapType) {
        return new RefType(true, heapType);
    }

    public static RefType nonNullable(HeapType heapType) {
        return new RefType(false, heapType);
    }

    public static RefType funcRef() {
        return new RefType(true, HeapType.func());
    }

    public static RefType externRef() {
        return new RefType(true, HeapType.extern());
    }

    public static RefType anyRef() {
        return new RefType(true, HeapType.any());
    }

    public static RefType nullRef() {
        return new RefType(true, HeapType.none());
    }

    public static RefType nullExternRef() {
        return new RefType(true, HeapType.noExtern());
    }

    public static RefType nullFuncRef() {
        return new RefType(true, HeapType.noFunc());
    }

    public static RefType eqRef() {
        return new RefType(true, HeapType.eq());
    }

    public static RefType structRef() {
        return new RefType(true, HeapType.struct());
    }

    public static RefType arrayRef() {
        return new RefType(true, HeapType.array());
    }

    public static RefType i31Ref() {
        return new RefType(true, HeapType.i31());
    }

    @Override
    public <T extends Exception> void accept(WasmTypeVisitor<T> visitor) throws T {
        visitor.visitRefType(this);
    }
}
