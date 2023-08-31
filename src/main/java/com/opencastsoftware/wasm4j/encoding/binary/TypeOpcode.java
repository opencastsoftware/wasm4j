/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding.binary;

public enum TypeOpcode {
    I32((byte) 0x7F),
    I64((byte) 0x7E),
    F32((byte) 0x7D),
    F64((byte) 0x7C),
    V128((byte) 0x7B),
    I8((byte) 0x7A),
    I16((byte) 0x79),
    // 0x78 .. 0x77 reserved
    HEAP_FUNC((byte) 0x70),
    HEAP_EXTERN((byte) 0x6F),
    ANY((byte) 0x6E),
    EQ((byte) 0x6D),
    REF_NULLABLE((byte) 0x6C),
    REF((byte) 0x6B),
    I31((byte) 0x6A),
    NOFUNC((byte) 0x69),
    NOEXTERN((byte) 0x68),
    STRUCT((byte) 0x67),
    ARRAY((byte) 0x66),
    NONE((byte) 0x65),
    // 0x64 .. 0x5F reserved
    FUNC((byte) 0x60),
    STRUCT_FIELDS((byte) 0x5F),
    ARRAY_FIELDS((byte) 0x5E),
    // 0x5D .. 0x51 reserved
    SUB((byte) 0x50),
    REC((byte) 0x4F),
    SUB_FINAL((byte) 0x4E),
    // Apparently unused
    // RESULT((byte) 0x40)
    ;

    private final byte opcode;

    TypeOpcode(byte opcode) {
        this.opcode = opcode;
    }

    public byte opcode() {
        return opcode;
    }
}
