package com.opencastsoftware.wasm4j.instructions.memory;

public interface MemArgInstruction extends MemoryInstruction {
    int offset();
    int align();
}
