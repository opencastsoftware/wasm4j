/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.encoding;

import com.opencastsoftware.wasm4j.encoding.binary.LEB128;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.LongRange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class LEB128Test {
    void testUnsignedEncoding(int input, byte[] expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeUnsigned(output, input);
        assertArrayEquals(expected, output.toByteArray());
    }

    void testUnsignedEncoding(long input, byte[] expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeUnsigned(output, input);
        assertArrayEquals(expected, output.toByteArray());
    }

    void testSignedEncoding(int input, byte[] expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeSigned(output, input);
        assertArrayEquals(expected, output.toByteArray());
    }

    void testSignedEncoding(long input, byte[] expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeSigned(output, input);
        assertArrayEquals(expected, output.toByteArray());
    }

    @Test
    void unsignedEncoding() throws IOException {
        testUnsignedEncoding(42, new byte[]{(byte) 0x2A});
        testUnsignedEncoding(42L, new byte[]{(byte) 0x2A});
        testUnsignedEncoding(9001, new byte[]{(byte) 0xA9, (byte) 0x46});
        testUnsignedEncoding(9001L, new byte[]{(byte) 0xA9, (byte) 0x46});
        testUnsignedEncoding(624485, new byte[]{(byte) 0xE5, (byte) 0x8E, (byte) 0x26});
        testUnsignedEncoding(624485L, new byte[]{(byte) 0xE5, (byte) 0x8E, (byte) 0x26});

        // 0x00000000
        testUnsignedEncoding(0, new byte[]{(byte) 0x0});
        // 0x7FFFFFFF
        testUnsignedEncoding(Integer.MAX_VALUE, new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x07});
        // 0x80000000
        testUnsignedEncoding(Integer.MIN_VALUE, new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x08});
        // 0xFFFFFFFF
        testUnsignedEncoding(-1, new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x0F});

        // 0x0000000000000000
        testUnsignedEncoding(0L, new byte[]{(byte) 0x0});
        // 0x7FFFFFFFFFFFFFFF
        testUnsignedEncoding(Long.MAX_VALUE, new byte[]{
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x7F});
        // 0x8000000000000000
        testUnsignedEncoding(Long.MIN_VALUE, new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x01});
        // 0xFFFFFFFFFFFFFFFF
        testUnsignedEncoding(-1L, new byte[]{
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x01});
    }

    @Property
    void roundTripUnsignedInt(@ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) int expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeUnsigned(output, expected);
        var bytes = output.toByteArray();
        assertThat(bytes.length, is(lessThanOrEqualTo(5))); // ceil(32/7)
        var actual = LEB128.readUnsignedInt(bytes);
        assertThat(actual, is(equalTo(expected)));
    }

    @Property
    void roundTripUnsignedLong(@ForAll @LongRange(min = Long.MIN_VALUE, max = Long.MAX_VALUE) long expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeUnsigned(output, expected);
        var bytes = output.toByteArray();
        assertThat(bytes.length, is(lessThanOrEqualTo(10))); // ceil(64/7)
        var actual = LEB128.readUnsignedLong(bytes);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void signedEncoding() throws IOException {
        testSignedEncoding(42, new byte[]{(byte) 0x2A});
        testSignedEncoding(42L, new byte[]{(byte) 0x2A});
        testSignedEncoding(9001, new byte[]{(byte) 0xA9, (byte) 0xC6, (byte) 0x00});
        testSignedEncoding(9001L, new byte[]{(byte) 0xA9, (byte) 0xC6, (byte) 0x00});
        testSignedEncoding(624485, new byte[]{(byte) 0xE5, (byte) 0x8E, (byte) 0x26});
        testSignedEncoding(624485L, new byte[]{(byte) 0xE5, (byte) 0x8E, (byte) 0x26});

        testSignedEncoding(-42, new byte[]{(byte) 0x56});
        testSignedEncoding(-42L, new byte[]{(byte) 0x56});
        testSignedEncoding(-9001, new byte[]{(byte) 0xD7, (byte) 0xB9, (byte) 0x7F});
        testSignedEncoding(-9001L, new byte[]{(byte) 0xD7, (byte) 0xB9, (byte) 0x7F});
        testSignedEncoding(-123456, new byte[]{(byte) 0xC0, (byte) 0xBB, (byte) 0x78});
        testSignedEncoding(-123456L, new byte[]{(byte) 0xC0, (byte) 0xBB, (byte) 0x78});

        // 0x00000000
        testSignedEncoding(0, new byte[]{(byte) 0x0});
        // 0x7FFFFFFF
        testSignedEncoding(Integer.MAX_VALUE, new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x07});
        // 0x80000000
        testSignedEncoding(Integer.MIN_VALUE, new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x78});
        // 0xFFFFFFFF
        testSignedEncoding(-1, new byte[]{(byte) 0x7F});

        // 0x0000000000000000
        testSignedEncoding(0L, new byte[]{(byte) 0x0});
        // 0x7FFFFFFFFFFFFFFF
        testSignedEncoding(Long.MAX_VALUE, new byte[]{
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00});
        // 0x8000000000000000
        testSignedEncoding(Long.MIN_VALUE, new byte[]{
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
                (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x7F});
        // 0xFFFFFFFFFFFFFFFF
        testSignedEncoding(-1L, new byte[]{(byte) 0x7F});
    }

    @Property
    void roundTripSignedInt(@ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) int expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeSigned(output, expected);
        var bytes = output.toByteArray();
        assertThat(bytes.length, is(lessThanOrEqualTo(5))); // ceil(32/7)
        var actual = LEB128.readSignedInt(bytes);
        assertThat(actual, is(equalTo(expected)));
    }

    @Property
    void roundTripSignedLong(@ForAll @LongRange(min = Long.MIN_VALUE, max = Long.MAX_VALUE) long expected) throws IOException {
        var output = new ByteArrayOutputStream();
        LEB128.writeSigned(output, expected);
        var bytes = output.toByteArray();
        assertThat(bytes.length, is(lessThanOrEqualTo(10))); // ceil(64/7)
        var actual = LEB128.readSignedLong(bytes);
        assertThat(actual, is(equalTo(expected)));
    }
}
