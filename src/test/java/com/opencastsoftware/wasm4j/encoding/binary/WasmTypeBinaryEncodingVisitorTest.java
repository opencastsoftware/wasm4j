package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.types.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WasmTypeBinaryEncodingVisitorTest {
    @Test
    void testTypeIdEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(HeapType.typeId(-1));
        assertArrayEquals(new byte[]{0x7F}, output.toByteArray());
    }

    @Test
    void testHeapFuncEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(HeapFuncType.INSTANCE);
        assertArrayEquals(new byte[]{TypeOpcode.HEAP_FUNC.opcode()}, output.toByteArray());
    }

    @Test
    void testHeapExternEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(HeapExternType.INSTANCE);
        assertArrayEquals(new byte[]{TypeOpcode.HEAP_EXTERN.opcode()}, output.toByteArray());
    }

    @Test
    void testLimitsEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);

        visitor.visitLimits(Limits.of(0));
        visitor.visitLimits(Limits.of(0, 5));
        visitor.visitLimits(Limits.of(1));
        visitor.visitLimits(Limits.of(1, 5));

        assertArrayEquals(new byte[]{
                // No max, no minimum
                (byte) 0x00, (byte) 0x00,
                // Max 5, no minimum
                (byte) 0x01, (byte) 0x00, 0x05,
                // No max, min 1
                (byte) 0x00, (byte) 0x01,
                // Max 5, min 1
                (byte) 0x01, (byte) 0x01, (byte) 0x05}, output.toByteArray());
    }

    @Test
    void testTableTypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);

        visitor.visitType(
                ExternType.table(Limits.of(0), RefType.heapFunc()));
        visitor.visitType(
                ExternType.table(Limits.of(0), RefType.nullable(HeapType.typeId(-1))));

        assertArrayEquals(new byte[]{
                // Table type 1
                // Ref type shorthand
                TypeOpcode.HEAP_FUNC.opcode(),
                // Limits
                (byte) 0x00, (byte) 0x00,
                // Table type 2
                // Nullable index ref type
                TypeOpcode.REF_NULLABLE.opcode(), 0x7F,
                // Limits
                (byte) 0x00, (byte) 0x00
        }, output.toByteArray());
    }

    @Test
    void testMemTypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);

        visitor.visitType(ExternType.mem(Limits.of(0)));
        visitor.visitType(ExternType.mem(Limits.of(1, 5)));

        assertArrayEquals(new byte[]{
                // Mem type 1
                // Limits
                (byte) 0x00, (byte) 0x00,
                // Mem type 2
                // Limits
                (byte) 0x01, (byte) 0x01, (byte) 0x05
        }, output.toByteArray());
    }

    @Test
    void testGlobalTypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);

        visitor.visitType(
                GlobalType.immutable(RefType.nonNullable(HeapType.typeId(-1))));
        visitor.visitType(
                GlobalType.mutable(NumType.i32()));

        assertArrayEquals(new byte[]{
                // Global type 1
                // Non-nullable index ref type
                TypeOpcode.REF.opcode(), (byte) 0x7F,
                (byte) 0x00, // const
                // Global type 2
                // i32
                TypeOpcode.I32.opcode(),
                (byte) 0x01 // var
        }, output.toByteArray());
    }

    @Test
    void testI32TypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(NumType.i32());
        assertArrayEquals(new byte[]{TypeOpcode.I32.opcode()}, output.toByteArray());
    }

    @Test
    void testI64TypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(NumType.i64());
        assertArrayEquals(new byte[]{TypeOpcode.I64.opcode()}, output.toByteArray());
    }

    @Test
    void testF32TypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(NumType.f32());
        assertArrayEquals(new byte[]{TypeOpcode.F32.opcode()}, output.toByteArray());
    }

    @Test
    void testF64TypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(NumType.f64());
        assertArrayEquals(new byte[]{TypeOpcode.F64.opcode()}, output.toByteArray());
    }

    @Test
    void testRefTypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);

        visitor.visitType(RefType.nonNullable(HeapType.typeId(-1)));
        visitor.visitType(RefType.nullable(HeapType.typeId(5)));
        visitor.visitType(RefType.nullable(HeapType.extern()));
        visitor.visitType(RefType.nonNullable(HeapType.extern()));
        visitor.visitType(RefType.nullable(HeapType.func()));
        visitor.visitType(RefType.nonNullable(HeapType.func()));

        assertArrayEquals(new byte[]{
                // Ref type 1
                // Non-nullable index ref type
                TypeOpcode.REF.opcode(), (byte) 0x7F,
                // Ref type 2
                // Nullable index ref type
                TypeOpcode.REF_NULLABLE.opcode(), (byte) 0x05,
                // Ref type 3
                // Nullable heap extern type
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_EXTERN.opcode(),
                // Ref type 4
                // Heap extern type
                TypeOpcode.HEAP_EXTERN.opcode(),
                // Ref type 5
                // Nullable heap func type
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(),
                // Ref type 6
                // Heap func type
                TypeOpcode.HEAP_FUNC.opcode()
        }, output.toByteArray());
    }

    @Test
    void testV128TypeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var visitor = new WasmTypeBinaryEncodingVisitor(output);
        visitor.visitType(VecType.v128());
        assertArrayEquals(new byte[]{TypeOpcode.V128.opcode()}, output.toByteArray());
    }
}