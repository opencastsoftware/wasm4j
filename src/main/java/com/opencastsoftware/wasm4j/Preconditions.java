package com.opencastsoftware.wasm4j;

import java.math.BigInteger;

public class Preconditions {
    private static final long U32_MAX = 4_294_967_295L;
    private static final BigInteger U64_MAX = new BigInteger("18446744073709551615");

    public static void checkValidU32(String name, long value) {
        if (value < 0 || value > U32_MAX) {
            throw new IllegalArgumentException(name + " is not in range for u32");
        }
    }

    public static void checkValidU64(String name, BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) < 0 || value.compareTo(U64_MAX) > 0) {
            throw new IllegalArgumentException(name + " is not in range for u64");
        }
    }
}
