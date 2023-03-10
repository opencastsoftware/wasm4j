package com.opencastsoftware.wasm4j.instructions.reference;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;
import com.opencastsoftware.wasm4j.types.HeapType;

public interface ReferenceInstruction extends Instruction {

    <T extends Exception> void accept(ReferenceInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((ReferenceInstructionVisitor<T>) visitor);
    }

    static RefNull ref_null(HeapType heapType) {
        return new RefNull(heapType);
    }

    static RefFunc ref_func(int funcIndex) {
        return new RefFunc(funcIndex);
    }

    static RefIsNull ref_is_null() {
        return RefIsNull.INSTANCE;
    }

    static RefAsNonNull ref_as_non_null() {
        return RefAsNonNull.INSTANCE;
    }
}
