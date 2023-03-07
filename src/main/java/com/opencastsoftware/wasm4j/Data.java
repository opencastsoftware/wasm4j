package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;

import java.util.List;

public class Data {
    private final byte[] init;
    private final Mode mode;

    public Data(byte[] init, Mode mode) {
        this.init = init;
        this.mode = mode;
    }

    public byte[] init() {
        return init;
    }

    public Mode mode() {
        return mode;
    }

    public interface Mode {
        static Passive passive() {
            return Passive.INSTANCE;
        }

        static Active active(int memIndex, ConstantExpression offset) {
            return new Active(memIndex, offset);
        }

        enum Passive implements Mode {
            INSTANCE;
        }

        class Active implements Mode {
            private final int memIndex;
            private final ConstantExpression offset;

            public Active(int memIndex, ConstantExpression offset) {
                this.memIndex = memIndex;
                this.offset = offset;
            }

            public int memIndex() {
                return memIndex;
            }

            public ConstantExpression offset() {
                return offset;
            }
        }

    }
}
