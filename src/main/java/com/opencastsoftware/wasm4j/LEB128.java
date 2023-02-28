package com.opencastsoftware.wasm4j;

import java.io.ByteArrayOutputStream;

public interface LEB128 {
    static void writeUnsigned(ByteArrayOutputStream out, int i) {
        while (true) {
            if (i < 0x80) {
                out.write((byte) (i & 0x7F));
                break;
            } else {
                out.write((byte) (i & 0x7F | 0x80));
                i >>>= 7;
            }
        }
    }

    static int readUnsigned(byte[] in) {
        int result = 0;
        int shift = 0;

        for (byte value : in) {
            if ((value & 0x80) == 0) {
                result |= (((byte) value) << shift);
                break;
            } else {
                result |= (((byte) (value & 0x7F)) << shift);
            }
            shift += 7;
        }

        return result;
    }

    static void writeSigned(ByteArrayOutputStream out, int input) {
        while (true) {
            byte byteValue = (byte) (input & 0x7F);
            input >>= 7;
            if ((input == 0 && (byteValue & 0x40) == 0) ||
                    (input == -1 && (byteValue & 0x40) != 0)) {
                out.write(byteValue);
                break;
            } else {
                out.write(byteValue | 0x80);
            }
        }
    }

    static int readSigned(byte[] in) {
        int result = 0;
        int shift = 0;
        byte value = 0;

        for (int i = 0; i < in.length; i++) {
            value = in[i];
            result |= (((byte) (value & 0x7F)) << shift);
            shift += 7;

            if ((value & 0x80) == 0) {
                break;
            }
        }

        if (shift < Integer.SIZE && (value & 0x40) != 0) {
            result |= (~0 << shift);
        }

        return result;
    }
}
