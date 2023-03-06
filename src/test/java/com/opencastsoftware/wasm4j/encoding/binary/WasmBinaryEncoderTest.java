package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Data;
import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.NumType;
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
    void testEncodeEmptyDataCount() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeDataCount(output, Collections.emptyList());
        assertArrayEquals(new byte[]{0x0}, output.toByteArray());
    }

    @Test
    void testEncodeTypes() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();

        encoder.encodeTypes(output, List.of(
                // (Int, Int) -> Int
                new FuncType(List.of(NumType.i32(), NumType.i32()), List.of(NumType.i32()))));

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
    void testEncodeMultipleDataCount() throws IOException {
        var encoder = new WasmBinaryEncoder();
        var output = new ByteArrayOutputStream();
        encoder.encodeDataCount(output, List.of(
                new Data(new byte[]{}, Data.Mode.passive()),
                new Data(new byte[]{}, Data.Mode.passive())));
        assertArrayEquals(new byte[]{0x02}, output.toByteArray());
    }
}