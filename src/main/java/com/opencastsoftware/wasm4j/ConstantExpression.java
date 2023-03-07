package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.ConstantInstruction;
import com.opencastsoftware.wasm4j.instructions.ConstantInstructionVisitor;

import java.util.Collections;
import java.util.List;

public class ConstantExpression extends BaseExpression<ConstantInstruction> {
    public ConstantExpression(List<ConstantInstruction> instructions) {
        super(instructions);
    }

    public ConstantExpression(ConstantInstruction... instructions) {
        this(List.of(instructions));
    }

    public <T extends Exception> void accept(ConstantInstructionVisitor<T> visitor) throws T {
        visitor.visitConstantExpression(this);
    }

    public static ConstantExpression of(ConstantInstruction... instructions) {
        return new ConstantExpression(instructions);
    }

    public static ConstantExpression empty() {
        return new ConstantExpression(Collections.emptyList());
    }
}
