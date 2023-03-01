package com.opencastsoftware.wasm4j;

public enum TypeOpcode {
    I32((byte) 0x7F),
    I64((byte) 0x7E),
    F32((byte) 0x7D),
    F64((byte) 0x7C),
    V128((byte) 0x7B),
    // 0x7A .. 0x71 reserved
    FUNCREF((byte) 0x70),
    EXTERNREF((byte) 0x6F),
    // 0x6E .. 0x61 reserved
    FUNC((byte) 0x60),
    // 0x5F .. 0x41 reserved
    RESULT((byte) 0x40)
    ;

    private final byte opcode;

    TypeOpcode(byte opcode) {
        this.opcode = opcode;
    }

    public byte opcode() {
        return opcode;
    }
}
