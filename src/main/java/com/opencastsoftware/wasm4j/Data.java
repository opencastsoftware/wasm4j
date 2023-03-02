package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;

import java.util.List;

public class Data {
    private final byte[] init;
    private final Mode dataMode;

    public Data(byte[] init, Mode dataMode) {
        this.init = init;
        this.dataMode = dataMode;
    }

    public interface Mode {
        static Passive passive() {
            return Passive.INSTANCE;
        }

        static Active active(long memIndex, List<ConstantInstruction> offset) {
            return new Active(memIndex, offset);
        }

        enum Passive {
            INSTANCE;
        }

        class Active {
            private final long memIndex;
            private final List<ConstantInstruction> offset;

            public Active(long memIndex, List<ConstantInstruction> offset) {
                this.memIndex = memIndex;
                this.offset = offset;
            }

            public long memIndex() {
                return memIndex;
            }

            public List<ConstantInstruction> offset() {
                return offset;
            }
        }

    }
}
