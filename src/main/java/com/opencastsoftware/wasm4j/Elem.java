package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.RefType;

import java.util.List;

public class Elem {
    private final RefType type;
    private final List<ConstantExpression> init;
    private final Mode mode;

    public Elem(RefType type, List<ConstantExpression> init, Mode mode) {
        this.type = type;
        this.init = init;
        this.mode = mode;
    }

    public RefType type() {
        return type;
    }

    public List<ConstantExpression> init() {
        return init;
    }

    public Mode mode() {
        return mode;
    }

    public interface Mode {
        static Passive passive() {
            return Passive.INSTANCE;
        }

        static Active active(int tableIndex, ConstantExpression offset) {
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
            private final ConstantExpression offset;

            public Active(int tableIndex, ConstantExpression offset) {
                this.tableIndex = tableIndex;
                this.offset = offset;
            }

            public int tableIndex() {
                return tableIndex;
            }

            public ConstantExpression offset() {
                return offset;
            }
        }
    }
}
