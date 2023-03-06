package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.types.RefType;

import java.util.List;

public class Elem {
    private final RefType type;
    private final List<List<ConstantInstruction>> init;
    private final Mode elemMode;

    public Elem(RefType type, List<List<ConstantInstruction>> init, Mode elemMode) {
        this.type = type;
        this.init = init;
        this.elemMode = elemMode;
    }

    public RefType type() {
        return type;
    }

    public List<List<ConstantInstruction>> init() {
        return init;
    }

    public Mode elemMode() {
        return elemMode;
    }

    public interface Mode {
        static Passive passive() {
            return Passive.INSTANCE;
        }

        static Active active(int tableIndex, List<ConstantInstruction> offset) {
            return new Active(tableIndex, offset);
        }

        static Declarative declarative() {
            return Declarative.INSTANCE;
        }

        enum Passive implements Mode {
            INSTANCE;
        }

        enum Declarative implements Mode {
            INSTANCE;
        }

        class Active implements Mode {
            private final int tableIndex;
            private final List<ConstantInstruction> offset;

            public Active(int tableIndex, List<ConstantInstruction> offset) {
                this.tableIndex = tableIndex;
                this.offset = offset;
            }

            public int tableIndex() {
                return tableIndex;
            }

            public List<ConstantInstruction> offset() {
                return offset;
            }
        }
    }
}
