/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.reference.ReferenceInstruction;
import com.opencastsoftware.wasm4j.instructions.variable.VariableInstruction;
import com.opencastsoftware.wasm4j.types.HeapType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConstantInstructionBinaryEncodingVisitorTest {
    @Test
    void testI32ConstEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_const(16).accept(visitor);
        NumericInstruction.i32_const(0).accept(visitor);
        NumericInstruction.i32_const(-1).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.I32_CONST.opcode(),
                0x10,
                Opcode.I32_CONST.opcode(),
                0x00,
                Opcode.I32_CONST.opcode(),
                0x7F
        }, output.toByteArray());
    }

    @Test
    void testI64ConstEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_const(16L).accept(visitor);
        NumericInstruction.i64_const(0L).accept(visitor);
        NumericInstruction.i64_const(-1L).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.I64_CONST.opcode(),
                0x10,
                Opcode.I64_CONST.opcode(),
                0x00,
                Opcode.I64_CONST.opcode(),
                0x7F
        }, output.toByteArray());
    }

    @Test
    void testF32ConstEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_const(5.5f).accept(visitor);
        NumericInstruction.f32_const(0.0f).accept(visitor);
        NumericInstruction.f32_const(-0.1f).accept(visitor);
        NumericInstruction.f32_const(3.14f).accept(visitor);
        NumericInstruction.f32_const(Float.NaN).accept(visitor);

        assertArrayEquals(new byte[]{
                // IEE754 single-precision floating point, in little endian format:
                // this means that the first 23 bits are the significand,
                // the following 8 bits are the biased exponent,
                // and the final bit is the sign bit.
                // See https://tmairi.github.io/posts/exploring-ieee-754-arithmetic/
                // for an exploration of floating point representation.
                // To extract this representation using Java you can do
                // `Integer.toHexString(Float.floatToRawIntBits(f))`.
                Opcode.F32_CONST.opcode(), // 0x40B00000
                (byte) 0x00, (byte) 0x00, (byte) 0xB0, (byte) 0x40,
                Opcode.F32_CONST.opcode(), // 0x00000000
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                Opcode.F32_CONST.opcode(), // 0xBDCCCCCD
                (byte) 0xCD, (byte) 0xCC, (byte) 0xCC, (byte) 0xBD,
                Opcode.F32_CONST.opcode(), // 0x4048F5C3
                (byte) 0xC3, (byte) 0xF5, (byte) 0x48, (byte) 0x40,
                Opcode.F32_CONST.opcode(), // 0x7FC00000
                (byte) 0x00, (byte) 0x00, (byte) 0xC0, (byte) 0x7F
        }, output.toByteArray());
    }

    @Test
    void testF64ConstEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_const(5.5).accept(visitor);
        NumericInstruction.f64_const(0.0).accept(visitor);
        NumericInstruction.f64_const(-0.1).accept(visitor);
        NumericInstruction.f64_const(3.14).accept(visitor);
        NumericInstruction.f64_const(Double.NaN).accept(visitor);

        assertArrayEquals(new byte[]{
                // IEE754 double-precision floating point, in little endian format:
                // this means that the first 52 bits are the significand,
                // the following 11 bits are the biased exponent,
                // and the final bit is the sign bit.
                // See https://tmairi.github.io/posts/exploring-ieee-754-arithmetic/
                // for an exploration of floating point representation.
                // To extract this representation using Java you can do
                // `Long.toHexString(Double.doubleToRawLongBits(d))`.
                Opcode.F64_CONST.opcode(), // 0x4016000000000000
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x40,
                Opcode.F64_CONST.opcode(), // 0x0000000000000000
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                Opcode.F64_CONST.opcode(), // 0xBFB999999999999A
                (byte) 0x9A, (byte) 0x99, (byte) 0x99, (byte) 0x99,
                (byte) 0x99, (byte) 0x99, (byte) 0xB9, (byte) 0xBF,
                Opcode.F64_CONST.opcode(), // 0x40091EB851EB851F
                (byte) 0x1F, (byte) 0x85, (byte) 0xEB, (byte) 0x51,
                (byte) 0xB8, (byte) 0x1E, (byte) 0x09, (byte) 0x40,
                Opcode.F64_CONST.opcode(), // 0x7FF8000000000000
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0xF8, (byte) 0x7F
        }, output.toByteArray());
    }

    @Test
    void testRefNullEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        ReferenceInstruction.ref_null(HeapType.typeId(16)).accept(visitor);
        ReferenceInstruction.ref_null(HeapType.typeId(0)).accept(visitor);
        ReferenceInstruction.ref_null(HeapType.typeId(-1)).accept(visitor);
        ReferenceInstruction.ref_null(HeapType.func()).accept(visitor);
        ReferenceInstruction.ref_null(HeapType.extern()).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.REF_NULL.opcode(),
                0x10,
                Opcode.REF_NULL.opcode(),
                0x00,
                Opcode.REF_NULL.opcode(),
                0x7F,
                Opcode.REF_NULL.opcode(),
                TypeOpcode.HEAP_FUNC.opcode(),
                Opcode.REF_NULL.opcode(),
                TypeOpcode.HEAP_EXTERN.opcode()
        }, output.toByteArray());
    }

    @Test
    void testRefFuncEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        ReferenceInstruction.ref_func(16).accept(visitor);
        ReferenceInstruction.ref_func(0).accept(visitor);
        ReferenceInstruction.ref_func(-1).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.REF_FUNC.opcode(),
                0x10,
                Opcode.REF_FUNC.opcode(),
                0x00,
                Opcode.REF_FUNC.opcode(),
                // Note difference with integer constants: function index is encoded as *unsigned* LEB128 u32
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x0F
        }, output.toByteArray());
    }

    @Test
    void testGlobalGetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new ConstantInstructionBinaryEncodingVisitor(output, typeVisitor);

        VariableInstruction.global_get(16).accept(visitor);
        VariableInstruction.global_get(0).accept(visitor);
        VariableInstruction.global_get(-1).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.GLOBAL_GET.opcode(),
                0x10,
                Opcode.GLOBAL_GET.opcode(),
                0x00,
                Opcode.GLOBAL_GET.opcode(),
                // Note difference with integer constants: global index is encoded as *unsigned* LEB128 u32
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x0F
        }, output.toByteArray());
    }
}
