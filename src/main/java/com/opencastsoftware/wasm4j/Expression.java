package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;

import java.util.Collections;
import java.util.List;

public class Expression extends BaseExpression<Instruction> {
    public Expression(List<Instruction> instructions) {
        super(instructions);
    }

    public Expression(Instruction... instructions) {
        this(List.of(instructions));
    }

    public <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        visitor.visitExpression(this);
    }

    public static Expression of(Instruction... instructions) {
        return new Expression(instructions);
    }

    public static Expression empty() {
        return new Expression(Collections.emptyList());
    }
}
