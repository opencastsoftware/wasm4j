package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.instructions.control.ControlInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.types.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class InstructionBinaryEncodingVisitorTest {
    @Test
    void testBlockEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.block(
                NumericInstruction.i32_const(1)
        ).accept(visitor);

        ControlInstruction.block(
                NumType.i32(),
                NumericInstruction.i32_const(2)
        ).accept(visitor);

        ControlInstruction.block(
                HeapType.typeId(-1),
                NumericInstruction.i32_const(3)
        ).accept(visitor);

        ControlInstruction.block(
                RefType.nullable(HeapType.typeId(-1)),
                NumericInstruction.i32_const(4)
        ).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.BLOCK.opcode(),
                0x40, // Empty type
                Opcode.I32_CONST.opcode(),
                0x01,
                Opcode.END.opcode(),
                Opcode.BLOCK.opcode(),
                TypeOpcode.I32.opcode(),
                Opcode.I32_CONST.opcode(),
                0x02,
                Opcode.END.opcode(),
                Opcode.BLOCK.opcode(),
                0x7F, // Type index type
                Opcode.I32_CONST.opcode(),
                0x03,
                Opcode.END.opcode(),
                Opcode.BLOCK.opcode(),
                TypeOpcode.REF_NULLABLE.opcode(), 0x7F, // Nullable type index type
                Opcode.I32_CONST.opcode(),
                0x04,
                Opcode.END.opcode(),
        }, output.toByteArray());
    }

    @Test
    void testBranchEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.br(1).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.BR.opcode(), 0x01}, output.toByteArray());
    }

    @Test
    void testBranchIfEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.br_if(1).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.BR_IF.opcode(), 0x01}, output.toByteArray());
    }

    @Test
    void testBranchOnNonNullEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.br_on_non_null(1).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.BR_ON_NON_NULL.opcode(), 0x01}, output.toByteArray());
    }

    @Test
    void testBranchOnNullEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.br_on_null(1).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.BR_ON_NULL.opcode(), 0x01}, output.toByteArray());
    }

    @Test
    void testBranchTableEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.br_table(new int[]{0, 1, 2, 3}, 4).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.BR_TABLE.opcode(),
                // Labels vec length (LEB128 u32)
                0x04,
                0x00, 0x01, 0x02, 0x03,
                0x04, // default label
        }, output.toByteArray());
    }

    @Test
    void testCallEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.call(64).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.CALL.opcode(), 0x40}, output.toByteArray());
    }

    @Test
    void testCallIndirectEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.call_indirect(1, 32).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.CALL_INDIRECT.opcode(), 0x20, 0x01}, output.toByteArray());
    }

    @Test
    void testCallRefEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.call_ref(33).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.CALL_REF.opcode(), 0x21}, output.toByteArray());
    }

    @Test
    void testIfEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.ifInstr(
                List.of(NumericInstruction.i32_const(1)),
                List.of(NumericInstruction.i32_const(0))).accept(visitor);

        ControlInstruction.ifInstr(
                NumType.i32(),
                List.of(NumericInstruction.i32_const(1)),
                List.of(NumericInstruction.i32_const(0))).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.IF.opcode(), 0x40, // Empty type
                Opcode.I32_CONST.opcode(), 0x01,
                Opcode.ELSE.opcode(),
                Opcode.I32_CONST.opcode(), 0x00,
                Opcode.END.opcode(),
                Opcode.IF.opcode(), TypeOpcode.I32.opcode(),
                Opcode.I32_CONST.opcode(), 0x01,
                Opcode.ELSE.opcode(),
                Opcode.I32_CONST.opcode(), 0x00,
                Opcode.END.opcode(),
        }, output.toByteArray());
    }

    @Test
    void testLoopEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.loop(List.of(
                ControlInstruction.call(1),
                ControlInstruction.br(0)
        )).accept(visitor);

        ControlInstruction.loop(NumType.i32(), List.of(
                ControlInstruction.call(1),
                ControlInstruction.br(0)
        )).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.LOOP.opcode(), 0x40, // Empty type
                Opcode.CALL.opcode(), 0x01,
                Opcode.BR.opcode(), 0x00,
                Opcode.END.opcode(),
                Opcode.LOOP.opcode(), TypeOpcode.I32.opcode(),
                Opcode.CALL.opcode(), 0x01,
                Opcode.BR.opcode(), 0x00,
                Opcode.END.opcode(),
        }, output.toByteArray());
    }

    @Test
    void testNopEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.nop().accept(visitor);

        assertArrayEquals(new byte[]{Opcode.NOP.opcode()}, output.toByteArray());
    }

    @Test
    void testReturnEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.ret().accept(visitor);

        assertArrayEquals(new byte[]{Opcode.RETURN.opcode()}, output.toByteArray());
    }

    @Test
    void testUnreachableEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.unreachable().accept(visitor);

        assertArrayEquals(new byte[]{Opcode.UNREACHABLE.opcode()}, output.toByteArray());
    }
}
