package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.instructions.control.ControlInstruction;
import com.opencastsoftware.wasm4j.instructions.memory.MemoryInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.parametric.ParametricInstruction;
import com.opencastsoftware.wasm4j.instructions.reference.ReferenceInstruction;
import com.opencastsoftware.wasm4j.instructions.table.TableInstruction;
import com.opencastsoftware.wasm4j.instructions.variable.VariableInstruction;
import com.opencastsoftware.wasm4j.types.HeapType;
import com.opencastsoftware.wasm4j.types.NumType;
import com.opencastsoftware.wasm4j.types.RefType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class InstructionBinaryEncodingVisitorTest {
    // Control instructions
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

        ControlInstruction.loop(
                ControlInstruction.call(1),
                ControlInstruction.br(0)
        ).accept(visitor);

        ControlInstruction.loop(
                NumType.i32(),
                ControlInstruction.call(1),
                ControlInstruction.br(0)
        ).accept(visitor);

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

        assertArrayEquals(Opcode.NOP.bytes(), output.toByteArray());
    }

    @Test
    void testReturnEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.ret().accept(visitor);

        assertArrayEquals(Opcode.RETURN.bytes(), output.toByteArray());
    }

    @Test
    void testUnreachableEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ControlInstruction.unreachable().accept(visitor);

        assertArrayEquals(Opcode.UNREACHABLE.bytes(), output.toByteArray());
    }

    // Numeric instructions
    @Test
    void testI32LoadEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.i32_load(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.I32_LOAD.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testI64LoadEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.i64_load(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.I64_LOAD.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testF32LoadEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.f32_load(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.F32_LOAD.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testF64LoadEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.f64_load(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.F64_LOAD.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testI32StoreEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.i32_store(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.I32_STORE.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testI64StoreEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.i64_store(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.I64_STORE.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testF32StoreEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.f32_store(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.F32_STORE.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testF64StoreEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.f64_store(1, 2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.F64_STORE.opcode(), 0x02, 0x01}, output.toByteArray());
    }

    @Test
    void testMemorySizeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.memory_size().accept(visitor);

        assertArrayEquals(new byte[]{Opcode.MEMORY_SIZE.opcode(), 0x00}, output.toByteArray());
    }

    @Test
    void testMemoryGrowEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.memory_grow().accept(visitor);

        assertArrayEquals(new byte[]{Opcode.MEMORY_GROW.opcode(), 0x00}, output.toByteArray());
    }

    @Test
    void testMemoryFillEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.memory_fill().accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.MEMORY_FILL.bytes()[0],
                Opcode.MEMORY_FILL.bytes()[1],
                0x00
        }, output.toByteArray());
    }

    @Test
    void testMemoryCopyEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.memory_copy().accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.MEMORY_COPY.bytes()[0],
                Opcode.MEMORY_COPY.bytes()[1],
                0x00,
                0x00
        }, output.toByteArray());
    }

    @Test
    void testMemoryInitEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.memory_init(2).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.MEMORY_INIT.bytes()[0],
                Opcode.MEMORY_INIT.bytes()[1],
                0x02,
                0x00
        }, output.toByteArray());
    }

    @Test
    void testDataDropEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        MemoryInstruction.data_drop(2).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.DATA_DROP.bytes()[0],
                Opcode.DATA_DROP.bytes()[1],
                0x02,
        }, output.toByteArray());
    }

    // Numeric instructions
    @Test
    void testI32ClzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_clz().accept(visitor);

        assertArrayEquals(Opcode.I32_CLZ.bytes(), output.toByteArray());
    }

    @Test
    void testI32CtzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_ctz().accept(visitor);

        assertArrayEquals(Opcode.I32_CTZ.bytes(), output.toByteArray());
    }

    @Test
    void testI32PopcntEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_popcnt().accept(visitor);

        assertArrayEquals(Opcode.I32_POPCNT.bytes(), output.toByteArray());
    }

    @Test
    void testI64ClzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_clz().accept(visitor);

        assertArrayEquals(Opcode.I64_CLZ.bytes(), output.toByteArray());
    }

    @Test
    void testI64CtzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_ctz().accept(visitor);

        assertArrayEquals(Opcode.I64_CTZ.bytes(), output.toByteArray());
    }

    @Test
    void testI64PopcntEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_popcnt().accept(visitor);

        assertArrayEquals(Opcode.I64_POPCNT.bytes(), output.toByteArray());
    }

    @Test
    void testF32AbsEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_abs().accept(visitor);

        assertArrayEquals(Opcode.F32_ABS.bytes(), output.toByteArray());
    }

    @Test
    void testF32CeilEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_ceil().accept(visitor);

        assertArrayEquals(Opcode.F32_CEIL.bytes(), output.toByteArray());
    }

    @Test
    void testF32FloorEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_floor().accept(visitor);

        assertArrayEquals(Opcode.F32_FLOOR.bytes(), output.toByteArray());
    }

    @Test
    void testF32NearestEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_nearest().accept(visitor);

        assertArrayEquals(Opcode.F32_NEAREST.bytes(), output.toByteArray());
    }

    @Test
    void testF32NegEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_neg().accept(visitor);

        assertArrayEquals(Opcode.F32_NEG.bytes(), output.toByteArray());
    }

    @Test
    void testF32SqrtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_sqrt().accept(visitor);

        assertArrayEquals(Opcode.F32_SQRT.bytes(), output.toByteArray());
    }

    @Test
    void testF32TruncEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_trunc().accept(visitor);

        assertArrayEquals(Opcode.F32_TRUNC.bytes(), output.toByteArray());
    }

    @Test
    void testF64AbsEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_abs().accept(visitor);

        assertArrayEquals(Opcode.F64_ABS.bytes(), output.toByteArray());
    }

    @Test
    void testF64CeilEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_ceil().accept(visitor);

        assertArrayEquals(Opcode.F64_CEIL.bytes(), output.toByteArray());
    }

    @Test
    void testF64FloorEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_floor().accept(visitor);

        assertArrayEquals(Opcode.F64_FLOOR.bytes(), output.toByteArray());
    }

    @Test
    void testF64NearestEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_nearest().accept(visitor);

        assertArrayEquals(Opcode.F64_NEAREST.bytes(), output.toByteArray());
    }

    @Test
    void testF64NegEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_neg().accept(visitor);

        assertArrayEquals(Opcode.F64_NEG.bytes(), output.toByteArray());
    }

    @Test
    void testF64SqrtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_sqrt().accept(visitor);

        assertArrayEquals(Opcode.F64_SQRT.bytes(), output.toByteArray());
    }

    @Test
    void testF64TruncEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_trunc().accept(visitor);

        assertArrayEquals(Opcode.F64_TRUNC.bytes(), output.toByteArray());
    }

    @Test
    void testI32AddEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_add().accept(visitor);

        assertArrayEquals(Opcode.I32_ADD.bytes(), output.toByteArray());
    }

    @Test
    void testI32AndEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_and().accept(visitor);

        assertArrayEquals(Opcode.I32_AND.bytes(), output.toByteArray());
    }

    @Test
    void testI32DivSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_div_s().accept(visitor);

        assertArrayEquals(Opcode.I32_DIV_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32DivUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_div_u().accept(visitor);

        assertArrayEquals(Opcode.I32_DIV_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32MulEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_mul().accept(visitor);

        assertArrayEquals(Opcode.I32_MUL.bytes(), output.toByteArray());
    }

    @Test
    void testI32OrEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_or().accept(visitor);

        assertArrayEquals(Opcode.I32_OR.bytes(), output.toByteArray());
    }

    @Test
    void testI32RemSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_rem_s().accept(visitor);

        assertArrayEquals(Opcode.I32_REM_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32RemUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_rem_u().accept(visitor);

        assertArrayEquals(Opcode.I32_REM_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32RotlEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_rotl().accept(visitor);

        assertArrayEquals(Opcode.I32_ROTL.bytes(), output.toByteArray());
    }

    @Test
    void testI32RotrEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_rotr().accept(visitor);

        assertArrayEquals(Opcode.I32_ROTR.bytes(), output.toByteArray());
    }

    @Test
    void testI32ShlEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_shl().accept(visitor);

        assertArrayEquals(Opcode.I32_SHL.bytes(), output.toByteArray());
    }

    @Test
    void testI32ShrSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_shr_s().accept(visitor);

        assertArrayEquals(Opcode.I32_SHR_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32ShrUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_shr_u().accept(visitor);

        assertArrayEquals(Opcode.I32_SHR_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32SubEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_sub().accept(visitor);

        assertArrayEquals(Opcode.I32_SUB.bytes(), output.toByteArray());
    }

    @Test
    void testI32XorEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_xor().accept(visitor);

        assertArrayEquals(Opcode.I32_XOR.bytes(), output.toByteArray());
    }

    @Test
    void testI64AddEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_add().accept(visitor);

        assertArrayEquals(Opcode.I64_ADD.bytes(), output.toByteArray());
    }

    @Test
    void testI64AndEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_and().accept(visitor);

        assertArrayEquals(Opcode.I64_AND.bytes(), output.toByteArray());
    }

    @Test
    void testI64DivSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_div_s().accept(visitor);

        assertArrayEquals(Opcode.I64_DIV_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64DivUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_div_u().accept(visitor);

        assertArrayEquals(Opcode.I64_DIV_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64MulEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_mul().accept(visitor);

        assertArrayEquals(Opcode.I64_MUL.bytes(), output.toByteArray());
    }

    @Test
    void testI64OrEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_or().accept(visitor);

        assertArrayEquals(Opcode.I64_OR.bytes(), output.toByteArray());
    }

    @Test
    void testI64RemSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_rem_s().accept(visitor);

        assertArrayEquals(Opcode.I64_REM_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64RemUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_rem_u().accept(visitor);

        assertArrayEquals(Opcode.I64_REM_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64RotlEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_rotl().accept(visitor);

        assertArrayEquals(Opcode.I64_ROTL.bytes(), output.toByteArray());
    }

    @Test
    void testI64RotrEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_rotr().accept(visitor);

        assertArrayEquals(Opcode.I64_ROTR.bytes(), output.toByteArray());
    }

    @Test
    void testI64ShlEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_shl().accept(visitor);

        assertArrayEquals(Opcode.I64_SHL.bytes(), output.toByteArray());
    }

    @Test
    void testI64ShrSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_shr_s().accept(visitor);

        assertArrayEquals(Opcode.I64_SHR_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64ShrUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_shr_u().accept(visitor);

        assertArrayEquals(Opcode.I64_SHR_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64SubEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_sub().accept(visitor);

        assertArrayEquals(Opcode.I64_SUB.bytes(), output.toByteArray());
    }

    @Test
    void testI64XorEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_xor().accept(visitor);

        assertArrayEquals(Opcode.I64_XOR.bytes(), output.toByteArray());
    }

    @Test
    void testF32AddEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_add().accept(visitor);

        assertArrayEquals(Opcode.F32_ADD.bytes(), output.toByteArray());
    }

    @Test
    void testF32CopysignEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_copysign().accept(visitor);

        assertArrayEquals(Opcode.F32_COPYSIGN.bytes(), output.toByteArray());
    }

    @Test
    void testF32DivEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_div().accept(visitor);

        assertArrayEquals(Opcode.F32_DIV.bytes(), output.toByteArray());
    }

    @Test
    void testF32MaxEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_max().accept(visitor);

        assertArrayEquals(Opcode.F32_MAX.bytes(), output.toByteArray());
    }

    @Test
    void testF32MinEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_min().accept(visitor);

        assertArrayEquals(Opcode.F32_MIN.bytes(), output.toByteArray());
    }

    @Test
    void testF32MulEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_mul().accept(visitor);

        assertArrayEquals(Opcode.F32_MUL.bytes(), output.toByteArray());
    }

    @Test
    void testF32SubEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_sub().accept(visitor);

        assertArrayEquals(Opcode.F32_SUB.bytes(), output.toByteArray());
    }

    @Test
    void testF64AddEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_add().accept(visitor);

        assertArrayEquals(Opcode.F64_ADD.bytes(), output.toByteArray());
    }

    @Test
    void testF64CopysignEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_copysign().accept(visitor);

        assertArrayEquals(Opcode.F64_COPYSIGN.bytes(), output.toByteArray());
    }

    @Test
    void testF64DivEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_div().accept(visitor);

        assertArrayEquals(Opcode.F64_DIV.bytes(), output.toByteArray());
    }

    @Test
    void testF64MaxEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_max().accept(visitor);

        assertArrayEquals(Opcode.F64_MAX.bytes(), output.toByteArray());
    }

    @Test
    void testF64MinEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_min().accept(visitor);

        assertArrayEquals(Opcode.F64_MIN.bytes(), output.toByteArray());
    }

    @Test
    void testF64MulEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_mul().accept(visitor);

        assertArrayEquals(Opcode.F64_MUL.bytes(), output.toByteArray());
    }

    @Test
    void testF64SubEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_sub().accept(visitor);

        assertArrayEquals(Opcode.F64_SUB.bytes(), output.toByteArray());
    }

    @Test
    void testI32EqzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_eqz().accept(visitor);

        assertArrayEquals(Opcode.I32_EQZ.bytes(), output.toByteArray());
    }

    @Test
    void testI64EqzEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_eqz().accept(visitor);

        assertArrayEquals(Opcode.I64_EQZ.bytes(), output.toByteArray());
    }

    @Test
    void testI32EqEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_eq().accept(visitor);

        assertArrayEquals(Opcode.I32_EQ.bytes(), output.toByteArray());
    }

    @Test
    void testI32GeSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_ge_s().accept(visitor);

        assertArrayEquals(Opcode.I32_GE_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32GeUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_ge_u().accept(visitor);

        assertArrayEquals(Opcode.I32_GE_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32GtSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_gt_s().accept(visitor);

        assertArrayEquals(Opcode.I32_GT_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32GtUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_gt_u().accept(visitor);

        assertArrayEquals(Opcode.I32_GT_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32LeSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_le_s().accept(visitor);

        assertArrayEquals(Opcode.I32_LE_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32LeUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_le_u().accept(visitor);

        assertArrayEquals(Opcode.I32_LE_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32LtSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_lt_s().accept(visitor);

        assertArrayEquals(Opcode.I32_LT_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32LtUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_lt_u().accept(visitor);

        assertArrayEquals(Opcode.I32_LT_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32NeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_ne().accept(visitor);

        assertArrayEquals(Opcode.I32_NE.bytes(), output.toByteArray());
    }

    @Test
    void testI64EqEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_eq().accept(visitor);

        assertArrayEquals(Opcode.I64_EQ.bytes(), output.toByteArray());
    }

    @Test
    void testI64GeSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_ge_s().accept(visitor);

        assertArrayEquals(Opcode.I64_GE_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64GeUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_ge_u().accept(visitor);

        assertArrayEquals(Opcode.I64_GE_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64GtSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_gt_s().accept(visitor);

        assertArrayEquals(Opcode.I64_GT_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64GtUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_gt_u().accept(visitor);

        assertArrayEquals(Opcode.I64_GT_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64LeSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_le_s().accept(visitor);

        assertArrayEquals(Opcode.I64_LE_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64LeUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_le_u().accept(visitor);

        assertArrayEquals(Opcode.I64_LE_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64LtSignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_lt_s().accept(visitor);

        assertArrayEquals(Opcode.I64_LT_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64LtUnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_lt_u().accept(visitor);

        assertArrayEquals(Opcode.I64_LT_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64NeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_ne().accept(visitor);

        assertArrayEquals(Opcode.I64_NE.bytes(), output.toByteArray());
    }

    @Test
    void testF32EqEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_eq().accept(visitor);

        assertArrayEquals(Opcode.F32_EQ.bytes(), output.toByteArray());
    }

    @Test
    void testF32GeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_ge().accept(visitor);

        assertArrayEquals(Opcode.F32_GE.bytes(), output.toByteArray());
    }

    @Test
    void testF32GtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_gt().accept(visitor);

        assertArrayEquals(Opcode.F32_GT.bytes(), output.toByteArray());
    }

    @Test
    void testF32LeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_le().accept(visitor);

        assertArrayEquals(Opcode.F32_LE.bytes(), output.toByteArray());
    }

    @Test
    void testF32LtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_lt().accept(visitor);

        assertArrayEquals(Opcode.F32_LT.bytes(), output.toByteArray());
    }

    @Test
    void testF32NeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_ne().accept(visitor);

        assertArrayEquals(Opcode.F32_NE.bytes(), output.toByteArray());
    }

    @Test
    void testF64EqEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_eq().accept(visitor);

        assertArrayEquals(Opcode.F64_EQ.bytes(), output.toByteArray());
    }

    @Test
    void testF64GeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_ge().accept(visitor);

        assertArrayEquals(Opcode.F64_GE.bytes(), output.toByteArray());
    }

    @Test
    void testF64GtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_gt().accept(visitor);

        assertArrayEquals(Opcode.F64_GT.bytes(), output.toByteArray());
    }

    @Test
    void testF64LeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_le().accept(visitor);

        assertArrayEquals(Opcode.F64_LE.bytes(), output.toByteArray());
    }

    @Test
    void testF64LtEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_lt().accept(visitor);

        assertArrayEquals(Opcode.F64_LT.bytes(), output.toByteArray());
    }

    @Test
    void testF64NeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_ne().accept(visitor);

        assertArrayEquals(Opcode.F64_NE.bytes(), output.toByteArray());
    }

    @Test
    void testI32Extend8SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_extend8_s().accept(visitor);

        assertArrayEquals(Opcode.I32_EXTEND8_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64Extend8SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_extend8_s().accept(visitor);

        assertArrayEquals(Opcode.I64_EXTEND8_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32Extend16SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_extend16_s().accept(visitor);

        assertArrayEquals(Opcode.I32_EXTEND16_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64Extend16SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_extend16_s().accept(visitor);

        assertArrayEquals(Opcode.I64_EXTEND16_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64Extend32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_extend32_s().accept(visitor);

        assertArrayEquals(Opcode.I64_EXTEND32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32WrapI64Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_wrap_i64().accept(visitor);

        assertArrayEquals(Opcode.I32_WRAP_I64.bytes(), output.toByteArray());
    }

    @Test
    void testI64ExtendI32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_extend_i32_s().accept(visitor);

        assertArrayEquals(Opcode.I64_EXTEND_I32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64ExtendI32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_extend_i32_u().accept(visitor);

        assertArrayEquals(Opcode.I64_EXTEND_I32_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncF32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_f32_s().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_F32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncF32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_f32_u().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_F32_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncF64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_f64_s().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_F64_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncF64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_f64_u().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_F64_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncF32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_f32_s().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_F32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncF32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_f32_u().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_F32_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncF64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_f64_s().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_F64_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncF64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_f64_u().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_F64_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncSatF32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_sat_f32_s().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_SAT_F32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncSatF32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_sat_f32_u().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_SAT_F32_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncSatF64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_sat_f64_s().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_SAT_F64_S.bytes(), output.toByteArray());
    }

    @Test
    void testI32TruncSatF64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_trunc_sat_f64_u().accept(visitor);

        assertArrayEquals(Opcode.I32_TRUNC_SAT_F64_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncSatF32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_sat_f32_s().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_SAT_F32_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncSatF32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_sat_f32_u().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_SAT_F32_U.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncSatF64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_sat_f64_s().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_SAT_F64_S.bytes(), output.toByteArray());
    }

    @Test
    void testI64TruncSatF64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_trunc_sat_f64_u().accept(visitor);

        assertArrayEquals(Opcode.I64_TRUNC_SAT_F64_U.bytes(), output.toByteArray());
    }

    @Test
    void testF32DemoteF64Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_demote_f64().accept(visitor);

        assertArrayEquals(Opcode.F32_DEMOTE_F64.bytes(), output.toByteArray());
    }

    @Test
    void testF64PromoteF32Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_promote_f32().accept(visitor);

        assertArrayEquals(Opcode.F64_PROMOTE_F32.bytes(), output.toByteArray());
    }

    @Test
    void testF32ConvertI32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_convert_i32_s().accept(visitor);

        assertArrayEquals(Opcode.F32_CONVERT_I32_S.bytes(), output.toByteArray());
    }

    @Test
    void testF32ConvertI32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_convert_i32_u().accept(visitor);

        assertArrayEquals(Opcode.F32_CONVERT_I32_U.bytes(), output.toByteArray());
    }

    @Test
    void testF32ConvertI64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_convert_i64_s().accept(visitor);

        assertArrayEquals(Opcode.F32_CONVERT_I64_S.bytes(), output.toByteArray());
    }

    @Test
    void testF32ConvertI64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_convert_i64_u().accept(visitor);

        assertArrayEquals(Opcode.F32_CONVERT_I64_U.bytes(), output.toByteArray());
    }

    @Test
    void testF64ConvertI32SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_convert_i32_s().accept(visitor);

        assertArrayEquals(Opcode.F64_CONVERT_I32_S.bytes(), output.toByteArray());
    }

    @Test
    void testF64ConvertI32UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_convert_i32_u().accept(visitor);

        assertArrayEquals(Opcode.F64_CONVERT_I32_U.bytes(), output.toByteArray());
    }

    @Test
    void testF64ConvertI64SignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_convert_i64_s().accept(visitor);

        assertArrayEquals(Opcode.F64_CONVERT_I64_S.bytes(), output.toByteArray());
    }

    @Test
    void testF64ConvertI64UnsignedEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_convert_i64_u().accept(visitor);

        assertArrayEquals(Opcode.F64_CONVERT_I64_U.bytes(), output.toByteArray());
    }

    @Test
    void testI32ReinterpretF32Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i32_reinterpret_f32().accept(visitor);

        assertArrayEquals(Opcode.I32_REINTERPRET_F32.bytes(), output.toByteArray());
    }

    @Test
    void testI64ReinterpretF64Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.i64_reinterpret_f64().accept(visitor);

        assertArrayEquals(Opcode.I64_REINTERPRET_F64.bytes(), output.toByteArray());
    }

    @Test
    void testF32ReinterpretI32Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f32_reinterpret_i32().accept(visitor);

        assertArrayEquals(Opcode.F32_REINTERPRET_I32.bytes(), output.toByteArray());
    }

    @Test
    void testF64ReinterpretI64Encoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        NumericInstruction.f64_reinterpret_i64().accept(visitor);

        assertArrayEquals(Opcode.F64_REINTERPRET_I64.bytes(), output.toByteArray());
    }

    // Parametric instructions
    @Test
    void testDropEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ParametricInstruction.drop().accept(visitor);

        assertArrayEquals(Opcode.DROP.bytes(), output.toByteArray());
    }

    @Test
    void testSelectEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ParametricInstruction.select().accept(visitor);
        ParametricInstruction.select(NumType.i32()).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.SELECT.opcode(),
                Opcode.SELECT_TYPE.opcode(),
                0x01, // Select operand types vec length (LEB128 u32)
                TypeOpcode.I32.opcode()
        }, output.toByteArray());
    }

    @Test
    void testRefAsNonNullEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ReferenceInstruction.ref_as_non_null().accept(visitor);

        assertArrayEquals(Opcode.REF_AS_NON_NULL.bytes(), output.toByteArray());
    }

    @Test
    void testRefIsNullEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        ReferenceInstruction.ref_is_null().accept(visitor);

        assertArrayEquals(Opcode.REF_IS_NULL.bytes(), output.toByteArray());
    }

    // Table instructions
    @Test
    void testTableGetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_get(32).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.TABLE_GET.opcode(), 0x20}, output.toByteArray());
    }

    @Test
    void testTableSetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_set(2).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.TABLE_SET.opcode(), 0x02}, output.toByteArray());
    }

    @Test
    void testTableSizeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_size(4).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.TABLE_SIZE.bytes()[0],
                Opcode.TABLE_SIZE.bytes()[1],
                0x04
        }, output.toByteArray());
    }

    @Test
    void testTableGrowEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_grow(8).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.TABLE_GROW.bytes()[0],
                Opcode.TABLE_GROW.bytes()[1],
                0x08
        }, output.toByteArray());
    }

    @Test
    void testTableFillEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_fill(16).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.TABLE_FILL.bytes()[0],
                Opcode.TABLE_FILL.bytes()[1],
                0x10
        }, output.toByteArray());
    }

    @Test
    void testTableCopyEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_copy(16, 32).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.TABLE_COPY.bytes()[0],
                Opcode.TABLE_COPY.bytes()[1],
                0x10, 0x20
        }, output.toByteArray());
    }

    @Test
    void testTableInitEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.table_init(16, 32).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.TABLE_INIT.bytes()[0],
                Opcode.TABLE_INIT.bytes()[1],
                0x20, 0x10
        }, output.toByteArray());
    }

    @Test
    void testElemDropEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        TableInstruction.elem_drop(32).accept(visitor);

        assertArrayEquals(new byte[]{
                Opcode.ELEM_DROP.bytes()[0],
                Opcode.ELEM_DROP.bytes()[1],
                0x20
        }, output.toByteArray());
    }

    // Variable instructions
    @Test
    void testGlobalSetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        VariableInstruction.global_set(5).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.GLOBAL_SET.opcode(), 0x05}, output.toByteArray());
    }

    @Test
    void testLocalGetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        VariableInstruction.local_get(7).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.LOCAL_GET.opcode(), 0x07}, output.toByteArray());
    }

    @Test
    void testLocalSetEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        VariableInstruction.local_set(6).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.LOCAL_SET.opcode(), 0x06}, output.toByteArray());
    }

    @Test
    void testLocalTeeEncoding() throws IOException {
        var output = new ByteArrayOutputStream();
        var typeVisitor = new WasmTypeBinaryEncodingVisitor(output);
        var visitor = new InstructionBinaryEncodingVisitor(output, typeVisitor);

        VariableInstruction.local_tee(3).accept(visitor);

        assertArrayEquals(new byte[]{Opcode.LOCAL_TEE.opcode(), 0x03}, output.toByteArray());
    }
}
