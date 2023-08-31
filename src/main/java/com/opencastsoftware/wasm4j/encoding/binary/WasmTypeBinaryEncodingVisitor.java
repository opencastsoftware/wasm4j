/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.types.*;

import java.io.IOException;
import java.io.OutputStream;

public class WasmTypeBinaryEncodingVisitor implements WasmTypeVisitor<IOException> {
    private final OutputStream output;

    public WasmTypeBinaryEncodingVisitor(OutputStream output) {
        this.output = output;
    }

    @Override
    public void visitTypeId(TypeId typeId) throws IOException {
        LEB128.writeSigned(output, typeId.typeIndex());
    }

    @Override
    public void visitHeapFunc(HeapFuncType heapFunc) throws IOException {
        output.write(TypeOpcode.HEAP_FUNC.opcode());
    }

    @Override
    public void visitHeapExtern(HeapExternType heapExtern) throws IOException {
        output.write(TypeOpcode.HEAP_EXTERN.opcode());
    }

    @Override
    public void visitAnyType(AnyType any) throws IOException {
        output.write(TypeOpcode.ANY.opcode());
    }

    @Override
    public void visitNoneType(NoneType none) throws IOException {
        output.write(TypeOpcode.NONE.opcode());
    }

    @Override
    public void visitNoExternType(NoExternType noExtern) throws IOException {
        output.write(TypeOpcode.NOEXTERN.opcode());
    }

    @Override
    public void visitNoFuncType(NoFuncType noFunc) throws IOException {
        output.write(TypeOpcode.NOFUNC.opcode());
    }

    @Override
    public void visitEqType(EqType eq) throws IOException {
        output.write(TypeOpcode.EQ.opcode());
    }

    @Override
    public void visitStructType(StructType struct) throws IOException {
        output.write(TypeOpcode.STRUCT.opcode());
    }

    @Override
    public void visitArrayType(ArrayType array) throws IOException {
        output.write(TypeOpcode.ARRAY.opcode());
    }

    @Override
    public void visitI31Type(I31Type i31) throws IOException {
        output.write(TypeOpcode.I31.opcode());
    }

    @Override
    public void visitLimits(Limits limits) throws IOException {
        if (limits.max() == null) {
            output.write(0x00);
            LEB128.writeUnsigned(output, limits.min());
        } else {
            output.write(0x01);
            LEB128.writeUnsigned(output, limits.min());
            LEB128.writeUnsigned(output, limits.max());
        }
    }

    @Override
    public void visitFuncType(FuncType func) throws IOException {
        output.write(TypeOpcode.FUNC.opcode());

        LEB128.writeUnsigned(output, func.arguments().size());
        for (ValType valType : func.arguments()) {
            visitValType(valType);
        }

        LEB128.writeUnsigned(output, func.results().size());
        for (ValType valType : func.results()) {
            visitValType(valType);
        }
    }

    @Override
    public void visitTableType(TableType table) throws IOException {
        visitRefType(table.refType());
        visitLimits(table.limits());
    }

    @Override
    public void visitMemType(MemType mem) throws IOException {
        visitLimits(mem.limits());
    }

    @Override
    public void visitGlobalType(GlobalType global) throws IOException {
        visitValType(global.valType());
        if (global.isMutable()) {
            output.write(0x01);
        } else {
            output.write(0x00);
        }
    }

    @Override
    public void visitI32Type(I32Type i32) throws IOException {
        output.write(TypeOpcode.I32.opcode());
    }

    @Override
    public void visitI64Type(I64Type i64) throws IOException {
        output.write(TypeOpcode.I64.opcode());
    }

    @Override
    public void visitF32Type(F32Type f32) throws IOException {
        output.write(TypeOpcode.F32.opcode());
    }

    @Override
    public void visitF64Type(F64Type f64) throws IOException {
        output.write(TypeOpcode.F64.opcode());
    }

    @Override
    public void visitRefType(RefType ref) throws IOException {
        if (!ref.isNullable()) {
            if (ref.heapType() instanceof TypeId) {
                output.write(TypeOpcode.REF.opcode());
                visitHeapType(ref.heapType());
            } else {
                // Short form for non-index, non-nullable heap type
                visitHeapType(ref.heapType());
            }
        } else {
            output.write(TypeOpcode.REF_NULLABLE.opcode());
            visitHeapType(ref.heapType());
        }
    }

    @Override
    public void visitV128Type(V128Type v128) throws IOException {
        output.write(TypeOpcode.V128.opcode());
    }
}
