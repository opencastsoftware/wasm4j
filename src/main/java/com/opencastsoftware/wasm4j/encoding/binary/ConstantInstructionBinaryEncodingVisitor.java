/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.ConstantExpression;
import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.F32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.F64Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I64Const;
import com.opencastsoftware.wasm4j.instructions.reference.RefFunc;
import com.opencastsoftware.wasm4j.instructions.reference.RefNull;
import com.opencastsoftware.wasm4j.instructions.variable.GlobalGet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConstantInstructionBinaryEncodingVisitor implements ConstantInstructionVisitor<IOException> {
    protected final OutputStream output;
    protected final WasmTypeBinaryEncodingVisitor typeVisitor;

    public ConstantInstructionBinaryEncodingVisitor(OutputStream output, WasmTypeBinaryEncodingVisitor typeVisitor) {
        this.output = output;
        this.typeVisitor = typeVisitor;
    }

    @Override
    public void visitI32Const(I32Const i32Const) throws IOException {
        output.write(Opcode.I32_CONST.bytes());
        LEB128.writeSigned(output, i32Const.value());
    }

    @Override
    public void visitI64Const(I64Const i64Const) throws IOException {
        output.write(Opcode.I64_CONST.bytes());
        LEB128.writeSigned(output, i64Const.value());
    }

    @Override
    public void visitF32Const(F32Const f32Const) throws IOException {
        var buffer = ByteBuffer
                .allocate(Float.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putFloat(f32Const.value());
        output.write(Opcode.F32_CONST.bytes());
        output.write(buffer.array());
    }

    @Override
    public void visitF64Const(F64Const f64Const) throws IOException {
        var buffer = ByteBuffer
                .allocate(Double.BYTES)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putDouble(f64Const.value());
        output.write(Opcode.F64_CONST.bytes());
        output.write(buffer.array());
    }

    @Override
    public void visitRefNull(RefNull refNull) throws IOException {
        output.write(Opcode.REF_NULL.bytes());
        refNull.heapType().accept(typeVisitor);
    }

    @Override
    public void visitRefFunc(RefFunc refFunc) throws IOException {
        output.write(Opcode.REF_FUNC.bytes());
        LEB128.writeUnsigned(output, refFunc.funcIndex());
    }

    @Override
    public void visitGlobalGet(GlobalGet globalGet) throws IOException {
        output.write(Opcode.GLOBAL_GET.bytes());
        LEB128.writeUnsigned(output, globalGet.globalIndex());
    }

    @Override
    public void visitConstantExpression(ConstantExpression constExpr) throws IOException {
        for (ConstantInstruction instr: constExpr.instructions()) {
            instr.accept(this);
        }

        output.write(Opcode.END.bytes());
    }
}
