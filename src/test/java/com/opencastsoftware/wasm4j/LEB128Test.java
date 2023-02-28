package com.opencastsoftware.wasm4j;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

public class LEB128Test {
    void testUnsignedEncoding(int input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    void testSignedEncoding(int input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    void unsignedEncoding() {
        testUnsignedEncoding(42, new byte[] { (byte) 0x2A });
        testUnsignedEncoding(9001, new byte[] { (byte) 0xA9, 0x46 });
        testUnsignedEncoding(624485, new byte[] { (byte) 0xE5, (byte) 0x8E, (byte) 0x26 });
    }

    @Property
    void roundTripUnsigned(@ForAll @IntRange(min = 0, max = Integer.MAX_VALUE) int input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        var actual = LEB128.readUnsigned(baos.toByteArray());
        assertThat(actual, is(equalTo(input)));
    }

    @Test
    void signedEncoding() {
        testSignedEncoding(-42, new byte[] { (byte) 0x56 });
        testSignedEncoding(-9001, new byte[] { (byte) 0xD7, (byte) 0xB9, (byte) 0x7F });
        testSignedEncoding(-123456, new byte[] { (byte) 0xC0, (byte) 0xBB, (byte) 0x78 });
    }

    @Property
    void roundTripSigned(@ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) int input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        var actual = LEB128.readSigned(baos.toByteArray());
        assertThat(actual, is(equalTo(input)));
    }
}
