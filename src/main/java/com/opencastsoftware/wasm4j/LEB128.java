package com.opencastsoftware.wasm4j;

import java.io.ByteArrayOutputStream;

public class LEB128 {
    private static final byte LOW_7_BITS = 0x7F;
    private static final int CONTINUATION_BIT = 0x80;
    private static final byte SIGN_BIT = 0x40;

    private LEB128() {
    }

    public static void writeUnsigned(ByteArrayOutputStream out, long i) {
        while (true) {
            if (i < CONTINUATION_BIT) {
                out.write((byte) (i & LOW_7_BITS));
                break;
            } else {
                out.write((byte) (i & LOW_7_BITS | CONTINUATION_BIT));
                i >>>= 7;
            }
        }
    }

    public static int readUnsignedInt(byte[] in) {
        int result = 0;
        int shift = 0;

        for (byte value : in) {
            if ((value & CONTINUATION_BIT) == 0) {
                result |= ((int) value << shift);
                break;
            } else {
                result |= ((value & LOW_7_BITS) << shift);
            }
            shift += 7;
        }

        return result;
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
