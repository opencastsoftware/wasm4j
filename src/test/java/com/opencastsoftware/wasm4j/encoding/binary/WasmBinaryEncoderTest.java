/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.*;
import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.reference.ReferenceInstruction;
import com.opencastsoftware.wasm4j.instructions.variable.VariableInstruction;
import com.opencastsoftware.wasm4j.types.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class WasmBinaryEncoderTest {
    @Test
    void testEncodeMagic() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeMagic(output);
        assertArrayEquals(WasmBinaryEncoder.WASM_MAGIC, output.toByteArray());
    }

    @Test
    void testEncodeVersion() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeVersion(output);
        assertArrayEquals(WasmBinaryEncoder.WASM_BINARY_FORMAT_VERSION, output.toByteArray());
    }

    @Test
    void testEncodeEmptyTypes() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeTypes(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeTypes() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeTypes(output, List.of(
                // (Int, Int) -> Int
                ExternType.func(List.of(NumType.i32(), NumType.i32()), List.of(NumType.i32()))));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.TYPE.id(),
                // Section size (LEB128 u32)
                0x07,
                // Function types vec length (LEB128 u32)
                0x01,
                // Entry 1
                TypeOpcode.FUNC.opcode(),
                // Arguments length (LEB128 u32)
                0x02,
                TypeOpcode.I32.opcode(),
                TypeOpcode.I32.opcode(),
                // Return types length (LEB128 u32)
                0x01,
                TypeOpcode.I32.opcode()}, output.toByteArray());
    }

    @Test
    void testEncodeEmptyImports() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeImports(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeImports() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeImports(output, List.of(
                new Import("A", "a", Import.Descriptor.func(16)),
                new Import("B", "b", Import.Descriptor.table(ExternType.table(Limits.of(1), RefType.heapExtern()))),
                new Import("C", "c", Import.Descriptor.mem(ExternType.mem(Limits.of(1, 5)))),
                new Import("D", "d", Import.Descriptor.global(ExternType.global(false, NumType.i32())))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.IMPORT.id(),
                // Section size (LEB128 u32)
                0x1E,
                // Imports vec length (LEB128 u32)
                0x04,
                // Entry 1
                0x01, // Module name length
                0x41, // "A"
                0x01, // Symbol name length
                0x61, // "a"
                0x00, // typeidx
                0x10, // index 16 (LEB128 u32)
                // Entry 2
                0x01, // Module name length
                0x42, // "B"
                0x01, // Symbol name length
                0x62, // "b"
                0x01, // tabletype
                TypeOpcode.HEAP_EXTERN.opcode(), // Ref type shorthand
                0x00, 0x01, // Limits
                // Entry 3
                0x01, // Module name length
                0x43, // "C"
                0x01, // Symbol name length
                0x63, // "c"
                0x02, // memtype
                0x01, 0x01, 0x05, // Limits
                // Entry 4
                0x01, // Module name length
                0x44, // "D"
                0x01, // Symbol name length
                0x64, // "d"
                0x03, // globaltype
                TypeOpcode.I32.opcode(), 0x00 // const
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyFunctions() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeFunctions(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeFunctions() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeFunctions(output, List.of(
                new Func(0, Collections.emptyList(), Expression.empty()),
                new Func(1, Collections.emptyList(), Expression.empty()),
                new Func(2, Collections.emptyList(), Expression.empty())
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.FUNCTION.id(),
                // Section size (LEB128 u32)
                0x04,
                // Functions vec length (LEB128 u32)
                0x03,
                // Entry 1
                0x00,
                // Entry 2
                0x01,
                // Entry 3
                0x02
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyTables() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeTables(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeTables() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeTables(output, List.of(
                new Table(
                        ExternType.table(Limits.of(1), RefType.nullable(HeapType.func())),
                        ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))),
                new Table(
                        ExternType.table(Limits.of(1, 5), RefType.nonNullable(HeapType.typeId(1))),
                        ConstantExpression.of(VariableInstruction.global_get(0)))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.TABLE.id(),
                // Section size (LEB128 u32)
                0x14,
                // Tables vec length (LEB128 u32)
                0x02,
                // Entry 1
                0x40, 0x00, // Table definition prolog
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(), // Nullable heap function reference type
                0x00, 0x01, // Limits
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_FUNC.opcode(), Opcode.END.opcode(), // Initializer expression
                // Entry 2
                0x40, 0x00, // table definition prolog
                TypeOpcode.REF.opcode(), 0x01, // Non-nullable index type
                0x01, 0x01, 0x05, // Limits
                Opcode.GLOBAL_GET.opcode(), 0x00, Opcode.END.opcode(), // Initializer expression
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyMemories() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeMemories(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeMemories() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeMemories(output, List.of(
                ExternType.mem(Limits.of(1)),
                ExternType.mem(Limits.of(1, 5))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.MEMORY.id(),
                // Section size (LEB128 u32)
                0x06,
                // Memories vec length (LEB128 u32)
                0x02,
                // Entry 1
                0x00, 0x01, // Limits
                // Entry 2
                0x01, 0x01, 0x05, // Limits
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyGlobals() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeGlobals(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeGlobals() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeGlobals(output, List.of(
                new Global(GlobalType.mutable(NumType.i32()), ConstantExpression.of(NumericInstruction.i32_const(42))),
                new Global(GlobalType.immutable(NumType.f64()), ConstantExpression.of(NumericInstruction.f64_const(3.14))),
                new Global(GlobalType.mutable(RefType.nullable(HeapType.extern())), ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.extern())))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.GLOBAL.id(),
                // Section size (LEB128 u32)
                0x18,
                // Globals vec length (LEB128 u32)
                0x03,
                // Entry 1
                TypeOpcode.I32.opcode(), 0x01, // var
                Opcode.I32_CONST.opcode(), 0x2A, Opcode.END.opcode(),
                // Entry 2
                TypeOpcode.F64.opcode(), 0x00, // const
                Opcode.F64_CONST.opcode(),
                (byte) 0x1F, (byte) 0x85, (byte) 0xEB, (byte) 0x51,
                (byte) 0xB8, (byte) 0x1E, (byte) 0x09, (byte) 0x40,
                Opcode.END.opcode(),
                // Entry 3
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_EXTERN.opcode(), 0x01, // var
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_EXTERN.opcode(), Opcode.END.opcode()
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyExports() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeExports(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeExports() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeExports(output, List.of(
                new Export("a", Export.Descriptor.func(0)),
                new Export("b", Export.Descriptor.table(1)),
                new Export("c", Export.Descriptor.mem(2)),
                new Export("d", Export.Descriptor.global(3))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.EXPORT.id(),
                // Section size (LEB128 u32)
                0x11,
                // Exports vec length (LEB128 u32)
                0x04,
                // Entry 1
                0x01, // Export name length
                0x61, // "a"
                0x00, // funcidx
                0x00, // Function index 0 (LEB128 u32)
                // Entry 2
                0x01, // Export name length
                0x62, // "b"
                0x01, // tableidx
                0x01, // Table index 1 (LEB128 u32)
                // Entry 3
                0x01, // Export name length
                0x63, // "c"
                0x02, // memidx
                0x02, // Memory index 2 (LEB128 u32)
                // Entry 3
                0x01, // Export name length
                0x64, // "d"
                0x03, // globalidx
                0x03  // Global index 3 (LEB128 u32)
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyStart() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeStart(output, null);
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeStart() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeStart(output, 64);

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.START.id(),
                // Section size (LEB128 u32)
                0x01,
                // Start function index (LEB128 u32)
                0x40
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyElems() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeElems(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeElems() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeElems(output, List.of(
                new Elem(
                        RefType.nullable(HeapType.func()),
                        List.of(ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))),
                        Elem.Mode.passive()),
                new Elem(
                        RefType.nullable(HeapType.func()),
                        List.of(ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))),
                        Elem.Mode.declarative()),
                new Elem(
                        RefType.nullable(HeapType.func()),
                        List.of(ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))),
                        Elem.Mode.active(0, ConstantExpression.of(NumericInstruction.i32_const(2)))),
                new Elem(
                        RefType.nullable(HeapType.func()),
                        List.of(ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))),
                        Elem.Mode.active(4, ConstantExpression.of(NumericInstruction.i32_const(2))))
        ));

        // The encoding of the preceding integer of each entry is as follows:

        //| Int | Binary | Bit 2        | Bit 1          | Bit 0      |
        //|-----------------------------------------------------------|
        //| 0   | 000    | element kind | no table index | active     |
        //| 1   | 001    | element kind | passive        | non-active |
        //| 2   | 010    | element kind | table index    | active     |
        //| 3   | 011    | element kind | declarative    | non-active |
        //| 4   | 100    | element type | no table index | active     |
        //| 5   | 101    | element type | passive        | non-active |
        //| 6   | 110    | element type | table index    | active     |
        //| 7   | 111    | element type | declarative    | non-active |

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.ELEMENT.id(),
                // Section size (LEB128 u32)
                0x25,
                // Elements vec length (LEB128 u32)
                0x04,
                // Entry 1
                0x05, // Passive segment declared with element type
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(), // Nullable heap function type
                0x01, // Initialiser instruction vec length (LEB128 u32)
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_FUNC.opcode(), Opcode.END.opcode(), // Init expression
                // Entry 2
                0x07, // Declarative segment declared with element type
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(), // Nullable heap function type
                0x01, // Initialiser instruction vec length (LEB128 u32)
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_FUNC.opcode(), Opcode.END.opcode(), // Init expression
                // Entry 3
                0x06, // Active segment declared with element type and explicit table index
                0x00, // Table index
                Opcode.I32_CONST.opcode(), 0x02, Opcode.END.opcode(), // Offset expression
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(), // Nullable heap function type
                0x01, // Initialiser instruction vec length (LEB128 u32)
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_FUNC.opcode(), Opcode.END.opcode(), // Init expression
                // Entry 4
                0x06, // Active segment declared with element type and explicit table index
                0x04, // Table index
                Opcode.I32_CONST.opcode(), 0x02, Opcode.END.opcode(), // Offset expression
                TypeOpcode.REF_NULLABLE.opcode(), TypeOpcode.HEAP_FUNC.opcode(), // Nullable heap function type
                0x01, // Initialiser instruction vec length (LEB128 u32)
                Opcode.REF_NULL.opcode(), TypeOpcode.HEAP_FUNC.opcode(), Opcode.END.opcode(), // Init expression
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyDataCount() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeDataCount(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeDataCount() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeDataCount(output, List.of(
                new Data(new byte[]{}, Data.Mode.passive()),
                new Data(new byte[]{}, Data.Mode.passive())));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.DATA_COUNT.id(),
                // Section size (LEB128 u32)
                0x01,
                // Data count
                0x02 // 2 entries
        }, output.toByteArray());
    }


    @Test
    void testEncodeEmptyCode() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeCode(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeCode() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeCode(output, List.of(
                new Func(0, List.of(NumType.i32()), Expression.of(
                        VariableInstruction.local_get(0),
                        VariableInstruction.local_get(0),
                        NumericInstruction.i32_add())),
                new Func(1, List.of(NumType.i32(), NumType.i32()), Expression.of(
                        VariableInstruction.local_get(0),
                        VariableInstruction.local_get(1),
                        NumericInstruction.i32_add())),
                new Func(2, List.of(NumType.i32(), NumType.i32()), Expression.of(
                        VariableInstruction.local_get(0),
                        VariableInstruction.local_get(1),
                        NumericInstruction.i32_mul()))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.CODE.id(),
                // Section size (LEB128 u32)
                0x23,
                // Elements vec length (LEB128 u32)
                0x03, // 3 entries
                // Entry 1
                0x09, // Entry 1 code size (LEB128 u32)
                0x01, // Local variables vec length (LEB128 u32)
                0x01, TypeOpcode.I32.opcode(), // Number of variables of this type, variable type
                Opcode.LOCAL_GET.opcode(), 0x00,
                Opcode.LOCAL_GET.opcode(), 0x00,
                Opcode.I32_ADD.opcode(),
                Opcode.END.opcode(),
                // Entry 2
                0x0B, // Entry 2 code size (LEB128 u32)
                0x02, // Local variables vec length (LEB128 u32)
                // TODO: Use compressed local variable declaration format
                0x01, TypeOpcode.I32.opcode(), // Number of variables of this type, variable type
                0x01, TypeOpcode.I32.opcode(), // Number of variables of this type, variable type
                Opcode.LOCAL_GET.opcode(), 0x00,
                Opcode.LOCAL_GET.opcode(), 0x01,
                Opcode.I32_ADD.opcode(),
                Opcode.END.opcode(),
                // Entry 3
                0x0B, // Entry 3 code size (LEB128 u32)
                0x02, // Local variables vec length (LEB128 u32)
                // TODO: Use compressed local variable declaration format
                0x01, TypeOpcode.I32.opcode(), // Number of variables of this type, variable type
                0x01, TypeOpcode.I32.opcode(), // Number of variables of this type, variable type
                Opcode.LOCAL_GET.opcode(), 0x00,
                Opcode.LOCAL_GET.opcode(), 0x01,
                Opcode.I32_MUL.opcode(),
                Opcode.END.opcode(),
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyData() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeData(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeData() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeData(output, List.of(
                new Data(WasmBinaryEncoder.WASM_MAGIC, Data.Mode.passive()),
                new Data(WasmBinaryEncoder.WASM_MAGIC, Data.Mode.active(0, ConstantExpression.of(NumericInstruction.i32_const(0)))),
                new Data(WasmBinaryEncoder.WASM_MAGIC, Data.Mode.active(1, ConstantExpression.of(NumericInstruction.i32_const(1))))
        ));

        assertArrayEquals(new byte[]{
                // Section ID
                SectionId.DATA.id(),
                // Section size (LEB128 u32)
                0x1A,
                // Data vec length (LEB128 u32)
                0x03,
                // Entry 1
                0x01, // Passive data segment
                0x04, // Data length
                0x00, 0x61, 0x73, 0x6D, // Data
                // Entry 2
                0x00, // Active data segment without explicit memory index
                Opcode.I32_CONST.opcode(), 0x00, Opcode.END.opcode(), // Offset expression
                0x04, // Data length
                0x00, 0x61, 0x73, 0x6D, // Data
                // Entry 3
                0x02, // Active data segment with explicit memory index
                0x01, // Memory index
                Opcode.I32_CONST.opcode(), 0x01, Opcode.END.opcode(), // Offset expression
                0x04, // Data length
                0x00, 0x61, 0x73, 0x6D, // Data
        }, output.toByteArray());
    }

    @Test
    void testEncodeEmptyModule() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeModule(output, Module.empty());

        assertArrayEquals(new byte[]{
                // WASM magic
                0x00, 0x61, 0x73, 0x6D,
                // WASM binary format version
                0x01, 0x00, 0x00, 0x00
        }, output.toByteArray());
    }
}
