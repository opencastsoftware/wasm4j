package com.opencastsoftware.wasm4j.instructions;

import com.opencastsoftware.wasm4j.instructions.numeric.floating.F32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.F64Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I64Const;
import com.opencastsoftware.wasm4j.instructions.reference.RefFunc;
import com.opencastsoftware.wasm4j.instructions.reference.RefNull;
import com.opencastsoftware.wasm4j.instructions.variable.GlobalGet;

public interface ConstantInstructionVisitor<T extends Exception> {
    default void visitConstantInstruction(ConstantInstruction constant) throws T {
        constant.accept(this);
    }

    void visitI32Const(I32Const i32Const) throws T;

    void visitI64Const(I64Const i64Const) throws T;

    void visitF32Const(F32Const f32Const) throws T;

    void visitF64Const(F64Const f64Const) throws T;

    void visitRefNull(RefNull refNull) throws T;

    void visitRefFunc(RefFunc refFunc) throws T;

    void visitGlobalGet(GlobalGet globalGet) throws T;
}
