package com.opencastsoftware.wasm4j.encoding.binary;

public enum SectionId {
    CUSTOM((byte) 0),
    TYPE((byte) 1),
    IMPORT((byte) 2),
    FUNCTION((byte) 3),
    TABLE((byte) 4),
    MEMORY((byte) 5),
    GLOBAL((byte) 6),
    EXPORT((byte) 7),
    START((byte) 8),
    ELEMENT((byte) 9),
    CODE((byte) 10),
    DATA((byte) 11),
    DATA_COUNT((byte) 12),
    ;

    private byte id;

    SectionId(byte id) {
        this.id = id;
    }

    public byte id() {
        return id;
    }
}
