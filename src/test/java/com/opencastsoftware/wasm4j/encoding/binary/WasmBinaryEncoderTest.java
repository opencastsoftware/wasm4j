package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Module;
import com.opencastsoftware.wasm4j.*;
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
                new Func(0, Collections.emptyList(), Collections.emptyList()),
                new Func(1, Collections.emptyList(), Collections.emptyList()),
                new Func(2, Collections.emptyList(), Collections.emptyList())
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
    void testEncodeEmptyElems() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeElems(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
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
    void testEncodeEmptyData() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeData(output, Collections.emptyList());
        assertArrayEquals(new byte[]{}, output.toByteArray());
    }

    @Test
    void testEncodeEmptyModule() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeModule(output, Module.empty());

        assertArrayEquals(new byte[]{
                // WASM magic
                0x00, 0x61, 0x71, 0x6D,
                // WASM binary format version
                0x01, 0x00, 0x00, 0x00
        }, output.toByteArray());
    }
}