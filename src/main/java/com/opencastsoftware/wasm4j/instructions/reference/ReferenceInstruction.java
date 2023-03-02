package com.opencastsoftware.wasm4j.instructions.reference;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.types.HeapType;

public interface ReferenceInstruction extends Instruction {
    static RefNull ref_null(HeapType heapType) {
        return new RefNull(heapType);
    }

    static RefFunc ref_func(long funcIndex) {
        return new RefFunc(funcIndex);
    }

    static RefIsNull ref_is_null() {
        return RefIsNull.INSTANCE;
    }

    static RefAsNonNull ref_as_non_null() {
        return RefAsNonNull.INSTANCE;
    }
}
