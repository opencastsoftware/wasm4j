/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.types;


public interface HeapType extends WasmType {
    static HeapFuncType func() {
        return HeapFuncType.INSTANCE;
    }

    static HeapExternType extern() {
        return HeapExternType.INSTANCE;
    }

    static AnyType any() {
        return AnyType.INSTANCE;
    }

    static NoneType none() {
        return NoneType.INSTANCE;
    }

    static NoExternType noExtern() {
        return NoExternType.INSTANCE;
    }

    static NoFuncType noFunc() {
        return NoFuncType.INSTANCE;
    }

    static EqType eq() {
        return EqType.INSTANCE;
    }

    static StructType struct() {
        return StructType.INSTANCE;
    }

    static ArrayType array() {
        return ArrayType.INSTANCE;
    }

    static I31Type i31() {
        return I31Type.INSTANCE;
    }

    static TypeId typeId(int typeIndex) {
        return new TypeId(typeIndex);
    }
}
