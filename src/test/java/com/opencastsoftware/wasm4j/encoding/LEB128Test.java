package com.opencastsoftware.wasm4j.encoding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import net.jqwik.api.Example;
import net.jqwik.api.constraints.BigRange;
import org.junit.jupiter.api.Test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.LongRange;

public class LEB128Test {
    void testUnsignedEncoding(int input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    void testUnsignedEncoding(long input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    void testSignedEncoding(int input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    void testSignedEncoding(long input, byte[] expected) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        assertArrayEquals(expected, baos.toByteArray());
    }

    @Test
    void unsignedEncoding() {
        testUnsignedEncoding(42, new byte[] { (byte) 0x2A });
        testUnsignedEncoding(42L, new byte[] { (byte) 0x2A });
        testUnsignedEncoding(9001, new byte[] { (byte) 0xA9, 0x46 });
        testUnsignedEncoding(9001L, new byte[] { (byte) 0xA9, 0x46 });
        testUnsignedEncoding(624485, new byte[] { (byte) 0xE5, (byte) 0x8E, (byte) 0x26 });
        testUnsignedEncoding(624485L, new byte[] { (byte) 0xE5, (byte) 0x8E, (byte) 0x26 });
    }

    @Property
    void roundTripUnsignedLong(@ForAll @LongRange(min = 0, max = Long.MAX_VALUE) long input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        var output = LEB128.readUnsignedLong(baos.toByteArray());
        assertThat(output, is(input));
    }

    @Property
    void roundTripBigInt(@ForAll @BigRange(min = "0", max = "18446744073709551615") BigInteger input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeUnsigned(baos, input);
        var output = LEB128.readUnsignedBigInt(baos.toByteArray());
        assertThat(output, is(input));
    }

    @Test
    void signedEncoding() {
        testSignedEncoding(-42, new byte[] { (byte) 0x56 });
        testSignedEncoding(-42L, new byte[] { (byte) 0x56 });
        testSignedEncoding(-9001, new byte[] { (byte) 0xD7, (byte) 0xB9, (byte) 0x7F });
        testSignedEncoding(-9001L, new byte[] { (byte) 0xD7, (byte) 0xB9, (byte) 0x7F });
        testSignedEncoding(-123456, new byte[] { (byte) 0xC0, (byte) 0xBB, (byte) 0x78 });
        testSignedEncoding(-123456L, new byte[] { (byte) 0xC0, (byte) 0xBB, (byte) 0x78 });
    }

    @Property
    void roundTripSigned(@ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MAX_VALUE) int input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        var actual = LEB128.readSignedInt(baos.toByteArray());
        assertThat(actual, is(equalTo(input)));
    }

    @Property
    void roundTripSignedLong(@ForAll @LongRange(min = Long.MIN_VALUE, max = Long.MAX_VALUE) long input) {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, input);
        var actual = LEB128.readSignedLong(baos.toByteArray());
        assertThat(actual, is(equalTo(input)));
    }

    @Example
    void roundTripSignedLong() {
        var baos = new ByteArrayOutputStream();
        LEB128.writeSigned(baos, -134217729L);
        var actual = LEB128.readSignedLong(baos.toByteArray());
        assertThat(actual, is(equalTo(-134217729L)));
    }
}
