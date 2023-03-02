package com.opencastsoftware.wasm4j.encoding;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

public class LEB128 {
    private static final byte LOW_7_BITS = 0x7F;
    private static final BigInteger LOW_7_BITS_BIGINT = BigInteger.valueOf(LOW_7_BITS);
    private static final int CONTINUATION_BIT = 0x80;
    private static final BigInteger CONTINUATION_BIT_BIGINT = BigInteger.valueOf(CONTINUATION_BIT);
    private static final byte SIGN_BIT = 0x40;

    private LEB128() {
    }

    public static void writeUnsigned(ByteArrayOutputStream out, long l) {
        while (true) {
            if (l < CONTINUATION_BIT) {
                out.write((byte) (l & LOW_7_BITS));
                break;
            } else {
                out.write((byte) (l & LOW_7_BITS | CONTINUATION_BIT));
                l >>>= 7;
            }
        }
    }

    public static void writeUnsigned(ByteArrayOutputStream out, BigInteger bigInt) {
        while (true) {
            if (bigInt.compareTo(CONTINUATION_BIT_BIGINT) < 0) {
                BigInteger low7Bits = bigInt.and(LOW_7_BITS_BIGINT);
                out.write(low7Bits.byteValue());
                break;
            } else {
                BigInteger low7Bits = bigInt.and(LOW_7_BITS_BIGINT).or(CONTINUATION_BIT_BIGINT);
                out.write(low7Bits.byteValue());
                bigInt = bigInt.shiftRight(7);
            }
        }
    }

    public static long readUnsignedLong(byte[] in) {
        long result = 0;
        int shift = 0;

        for (byte value : in) {
            if ((value & CONTINUATION_BIT) == 0) {
                result |= ((long) value << shift);
                break;
            } else {
                result |= (((long) (value & LOW_7_BITS)) << shift);
            }
            shift += 7;
        }

        return result;
    }

    public static BigInteger readUnsignedBigInt(byte[] in) {
        BigInteger result = BigInteger.ZERO;
        int shift = 0;

        for (byte value : in) {
            if ((value & CONTINUATION_BIT) == 0) {
                result = result.or(BigInteger.valueOf(value).shiftLeft(shift));
                break;
            } else {
                result = result.or(BigInteger.valueOf(value & LOW_7_BITS).shiftLeft(shift));
            }
            shift += 7;
        }

        return result;
    }

    public static void writeSigned(ByteArrayOutputStream out, long longValue) {
        while (true) {
            byte byteValue = (byte) (((byte) longValue) & LOW_7_BITS);
            longValue >>= 7;

            boolean more = !((longValue == 0 && ((byteValue & SIGN_BIT) == 0)) ||
                    (longValue == -1 && ((byteValue & SIGN_BIT) != 0)));

            if (more) {
                byteValue |= CONTINUATION_BIT;
            }

            out.write(byteValue);

            if (!more) {
                break;
            }
        }
    }

    public static int readSignedInt(byte[] in) {
        int result = 0;
        int shift = 0;
        byte value = 0;

        for (byte b : in) {
            value = b;
            result |= ((value & LOW_7_BITS) << shift);
            shift += 7;

            if ((value & CONTINUATION_BIT) == 0) {
                break;
            }
        }

        if (shift < Integer.SIZE && (value & SIGN_BIT) != 0) {
            result |= (~0 << shift);
        }

        return result;
    }

    public static long readSignedLong(byte[] in) {
        long result = 0L;
        int shift = 0;
        byte value = 0;

        for (byte b : in) {
            value = b;
            result |= (((long) (value & LOW_7_BITS)) << shift);
            shift += 7;

            if ((value & CONTINUATION_BIT) == 0) {
                break;
            }
        }

        if (shift < Long.SIZE && ((value & SIGN_BIT) != 0)) {
            result |= (~0L << shift);
        }

        return result;
    }
}
