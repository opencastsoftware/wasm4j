package com.opencastsoftware.wasm4j.encoding.binary;

import com.opencastsoftware.wasm4j.Expression;
import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;
import com.opencastsoftware.wasm4j.instructions.control.*;
import com.opencastsoftware.wasm4j.instructions.memory.*;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.binary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.relational.*;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.unary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.binary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.conversion.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.relational.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.test.I32Eqz;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.test.I64Eqz;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.unary.*;
import com.opencastsoftware.wasm4j.instructions.parametric.Drop;
import com.opencastsoftware.wasm4j.instructions.parametric.Select;
import com.opencastsoftware.wasm4j.instructions.reference.RefAsNonNull;
import com.opencastsoftware.wasm4j.instructions.reference.RefIsNull;
import com.opencastsoftware.wasm4j.instructions.table.*;
import com.opencastsoftware.wasm4j.instructions.variable.GlobalSet;
import com.opencastsoftware.wasm4j.instructions.variable.LocalGet;
import com.opencastsoftware.wasm4j.instructions.variable.LocalSet;
import com.opencastsoftware.wasm4j.instructions.variable.LocalTee;
import com.opencastsoftware.wasm4j.types.BlockType;
import com.opencastsoftware.wasm4j.types.TypeId;
import com.opencastsoftware.wasm4j.types.ValType;

import java.io.IOException;
import java.io.OutputStream;

public class InstructionBinaryEncodingVisitor extends ConstantInstructionBinaryEncodingVisitor implements InstructionVisitor<IOException> {
    public InstructionBinaryEncodingVisitor(OutputStream output, WasmTypeBinaryEncodingVisitor typeVisitor) {
        super(output, typeVisitor);
    }

    // Control instructions
    private void visitBlockType(BlockType blockType) throws IOException {
        if (blockType instanceof TypeId) {
            var typeId = (TypeId) blockType;
            LEB128.writeSigned(output, typeId.typeIndex());
        } else if (blockType instanceof ValType) {
            var valType = (ValType) blockType;
            typeVisitor.visitValType(valType);
        } else {
            output.write(0x40); // Empty type
        }
    }

    @Override
    public void visitBlock(Block block) throws IOException {
        output.write(Opcode.BLOCK.opcode());

        visitBlockType(block.blockType());

        for (Instruction instr: block.instructions()) {
            instr.accept(this);
        }

        output.write(Opcode.END.opcode());
    }

    @Override
    public void visitBranch(Branch branch) throws IOException {
        output.write(Opcode.BR.opcode());
        LEB128.writeUnsigned(output, branch.labelIndex());
    }

    @Override
    public void visitBranchIf(BranchIf branchIf) throws IOException {
        output.write(Opcode.BR_IF.opcode());
        LEB128.writeUnsigned(output, branchIf.labelIndex());
    }

    @Override
    public void visitBranchOnNonNull(BranchOnNonNull branchOnNonNull) throws IOException {
        output.write(Opcode.BR_ON_NON_NULL.opcode());
        LEB128.writeUnsigned(output, branchOnNonNull.labelIndex());
    }

    @Override
    public void visitBranchOnNull(BranchOnNull branchOnNull) throws IOException {
        output.write(Opcode.BR_ON_NULL.opcode());
        LEB128.writeUnsigned(output, branchOnNull.labelIndex());
    }

    @Override
    public void visitBranchTable(BranchTable branchTable) throws IOException {
        output.write(Opcode.BR_TABLE.opcode());

        LEB128.writeUnsigned(output, branchTable.labels().length);
        for (int label : branchTable.labels()) {
            LEB128.writeUnsigned(output, label);
        }

        LEB128.writeUnsigned(output, branchTable.defaultLabel());
    }

    @Override
    public void visitCall(Call call) throws IOException {
        output.write(Opcode.CALL.opcode());
        LEB128.writeUnsigned(output, call.funcIndex());
    }

    @Override
    public void visitCallIndirect(CallIndirect callIndirect) throws IOException {
        output.write(Opcode.CALL_INDIRECT.opcode());
        LEB128.writeUnsigned(output, callIndirect.typeIndex());
        LEB128.writeUnsigned(output, callIndirect.tableIndex());
    }

    @Override
    public void visitCallRef(CallRef callRef) throws IOException {
        output.write(Opcode.CALL_REF.opcode());
        LEB128.writeUnsigned(output, callRef.typeIndex());
    }

    @Override
    public void visitIf(If ifInstr) throws IOException {
        output.write(Opcode.IF.opcode());

        visitBlockType(ifInstr.blockType());

        for (Instruction instr: ifInstr.consequent()) {
            instr.accept(this);
        }

        output.write(Opcode.ELSE.opcode());

        for (Instruction instr: ifInstr.alternative()) {
            instr.accept(this);
        }

        output.write(Opcode.END.opcode());
    }

    @Override
    public void visitLoop(Loop loop) throws IOException {
        output.write(Opcode.LOOP.opcode());

        visitBlockType(loop.blockType());

        for (Instruction instr: loop.instructions()) {
            instr.accept(this);
        }

        output.write(Opcode.END.opcode());
    }

    @Override
    public void visitNop(Nop nop) throws IOException {
        output.write(Opcode.NOP.opcode());
    }

    @Override
    public void visitReturn(Return ret) throws IOException {
        output.write(Opcode.RETURN.opcode());
    }

    @Override
    public void visitUnreachable(Unreachable unreachable) throws IOException {
        output.write(Opcode.UNREACHABLE.opcode());
    }

    // Memory instructions
    private void visitMemArgInstruction(MemArgInstruction memArg) throws IOException {
        LEB128.writeUnsigned(output, memArg.align());
        LEB128.writeUnsigned(output, memArg.offset());
    }

    @Override
    public void visitI32Load(I32Load i32Load) throws IOException {
        output.write(Opcode.I32_LOAD.opcode());
        visitMemArgInstruction(i32Load);
    }

    @Override
    public void visitI64Load(I64Load i64Load) throws IOException {
        output.write(Opcode.I64_LOAD.opcode());
        visitMemArgInstruction(i64Load);
    }

    @Override
    public void visitF32Load(F32Load f32Load) throws IOException {
        output.write(Opcode.F32_LOAD.opcode());
        visitMemArgInstruction(f32Load);
    }

    @Override
    public void visitF64Load(F64Load f64Load) throws IOException {
        output.write(Opcode.F64_LOAD.opcode());
        visitMemArgInstruction(f64Load);
    }

    @Override
    public void visitI32Store(I32Store i32Store) throws IOException {
        output.write(Opcode.I32_STORE.opcode());
        visitMemArgInstruction(i32Store);
    }

    @Override
    public void visitI64Store(I64Store i64Store) throws IOException {
        output.write(Opcode.I64_STORE.opcode());
        visitMemArgInstruction(i64Store);
    }

    @Override
    public void visitF32Store(F32Store f32Store) throws IOException {
        output.write(Opcode.F32_STORE.opcode());
        visitMemArgInstruction(f32Store);
    }

    @Override
    public void visitF64Store(F64Store f64Store) throws IOException {
        output.write(Opcode.F64_STORE.opcode());
        visitMemArgInstruction(f64Store);
    }

    @Override
    public void visitMemorySize(MemorySize memorySize) throws IOException {
        output.write(Opcode.MEMORY_SIZE.opcode());
        LEB128.writeUnsigned(output, memorySize.memIndex());
    }

    @Override
    public void visitMemoryGrow(MemoryGrow memoryGrow) throws IOException {
        output.write(Opcode.MEMORY_GROW.opcode());
        LEB128.writeUnsigned(output, memoryGrow.memIndex());
    }

    @Override
    public void visitMemoryFill(MemoryFill memoryFill) throws IOException {
        output.write(Opcode.MEMORY_FILL.bytes());
        LEB128.writeUnsigned(output, memoryFill.memIndex());
    }

    @Override
    public void visitMemoryCopy(MemoryCopy memoryCopy) throws IOException {
        // TODO: Check parameter ordering once indexing additional memories is possible
        output.write(Opcode.MEMORY_COPY.bytes());
        LEB128.writeUnsigned(output, memoryCopy.targetMemIndex());
        LEB128.writeUnsigned(output, memoryCopy.sourceMemIndex());
    }

    @Override
    public void visitMemoryInit(MemoryInit memoryInit) throws IOException {
        output.write(Opcode.MEMORY_INIT.bytes());
        LEB128.writeUnsigned(output, memoryInit.dataIndex());
        LEB128.writeUnsigned(output, memoryInit.memIndex());
    }

    @Override
    public void visitDataDrop(DataDrop dataDrop) throws IOException {
        output.write(Opcode.DATA_DROP.bytes());
        LEB128.writeUnsigned(output, dataDrop.dataIndex());
    }

    // Numeric instructions
    @Override
    public void visitI32Clz(I32Clz i32Clz) throws IOException {
        output.write(Opcode.I32_CLZ.opcode());
    }

    @Override
    public void visitI32Ctz(I32Ctz i32Ctz) throws IOException {
        output.write(Opcode.I32_CTZ.opcode());
    }

    @Override
    public void visitI32Popcnt(I32Popcnt i32Popcnt) throws IOException {
        output.write(Opcode.I32_POPCNT.opcode());
    }

    @Override
    public void visitI64Clz(I64Clz i64Clz) throws IOException {
        output.write(Opcode.I64_CLZ.opcode());
    }

    @Override
    public void visitI64Ctz(I64Ctz i64Ctz) throws IOException {
        output.write(Opcode.I64_CTZ.opcode());
    }

    @Override
    public void visitI64Popcnt(I64Popcnt i64Popcnt) throws IOException {
        output.write(Opcode.I64_POPCNT.opcode());
    }

    @Override
    public void visitF32Abs(F32Abs f32Abs) throws IOException {
        output.write(Opcode.F32_ABS.opcode());
    }

    @Override
    public void visitF32Ceil(F32Ceil f32Ceil) throws IOException {
        output.write(Opcode.F32_CEIL.opcode());
    }

    @Override
    public void visitF32Floor(F32Floor f32Floor) throws IOException {
        output.write(Opcode.F32_FLOOR.opcode());
    }

    @Override
    public void visitF32Nearest(F32Nearest f32Nearest) throws IOException {
        output.write(Opcode.F32_NEAREST.opcode());
    }

    @Override
    public void visitF32Neg(F32Neg f32Neg) throws IOException {
        output.write(Opcode.F32_NEG.opcode());
    }

    @Override
    public void visitF32Sqrt(F32Sqrt f32Sqrt) throws IOException {
        output.write(Opcode.F32_SQRT.opcode());
    }

    @Override
    public void visitF32Trunc(F32Trunc f32Trunc) throws IOException {
        output.write(Opcode.F32_TRUNC.opcode());
    }

    @Override
    public void visitF64Abs(F64Abs f64Abs) throws IOException {
        output.write(Opcode.F64_ABS.opcode());
    }

    @Override
    public void visitF64Ceil(F64Ceil f64Ceil) throws IOException {
        output.write(Opcode.F64_CEIL.opcode());
    }

    @Override
    public void visitF64Floor(F64Floor f64Floor) throws IOException {
        output.write(Opcode.F64_FLOOR.opcode());
    }

    @Override
    public void visitF64Nearest(F64Nearest f64Nearest) throws IOException {
        output.write(Opcode.F64_NEAREST.opcode());
    }

    @Override
    public void visitF64Neg(F64Neg f64Neg) throws IOException {
        output.write(Opcode.F64_NEG.opcode());
    }

    @Override
    public void visitF64Sqrt(F64Sqrt f64Sqrt) throws IOException {
        output.write(Opcode.F64_SQRT.opcode());
    }

    @Override
    public void visitF64Trunc(F64Trunc f64Trunc) throws IOException {
        output.write(Opcode.F64_TRUNC.opcode());
    }

    @Override
    public void visitI32Add(I32Add i32Add) throws IOException {
        output.write(Opcode.I32_ADD.opcode());
    }

    @Override
    public void visitI32And(I32And i32And) throws IOException {
        output.write(Opcode.I32_AND.opcode());
    }

    @Override
    public void visitI32DivSigned(I32DivSigned i32DivSigned) throws IOException {
        output.write(Opcode.I32_DIV_S.opcode());
    }

    @Override
    public void visitI32DivUnsigned(I32DivUnsigned i32DivUnsigned) throws IOException {
        output.write(Opcode.I32_DIV_U.opcode());
    }

    @Override
    public void visitI32Mul(I32Mul i32Mul) throws IOException {
        output.write(Opcode.I32_MUL.opcode());
    }

    @Override
    public void visitI32Or(I32Or i32Or) throws IOException {
        output.write(Opcode.I32_OR.opcode());
    }

    @Override
    public void visitI32RemSigned(I32RemSigned i32RemSigned) throws IOException {
        output.write(Opcode.I32_REM_S.opcode());
    }

    @Override
    public void visitI32RemUnsigned(I32RemUnsigned i32RemUnsigned) throws IOException {
        output.write(Opcode.I32_REM_U.opcode());
    }

    @Override
    public void visitI32Rotl(I32Rotl i32Rotl) throws IOException {
        output.write(Opcode.I32_ROTL.opcode());
    }

    @Override
    public void visitI32Rotr(I32Rotr i32Rotr) throws IOException {
        output.write(Opcode.I32_ROTR.opcode());
    }

    @Override
    public void visitI32Shl(I32Shl i32Shl) throws IOException {
        output.write(Opcode.I32_SHL.opcode());
    }

    @Override
    public void visitI32ShrSigned(I32ShrSigned i32ShrSigned) throws IOException {
        output.write(Opcode.I32_SHR_S.opcode());
    }

    @Override
    public void visitI32ShrUnsigned(I32ShrUnsigned i32ShrUnsigned) throws IOException {
        output.write(Opcode.I32_SHR_U.opcode());
    }

    @Override
    public void visitI32Sub(I32Sub i32Sub) throws IOException {
        output.write(Opcode.I32_SUB.opcode());
    }

    @Override
    public void visitI32Xor(I32Xor i32Xor) throws IOException {
        output.write(Opcode.I32_XOR.opcode());
    }

    @Override
    public void visitI64Add(I64Add i64Add) throws IOException {
        output.write(Opcode.I64_ADD.opcode());
    }

    @Override
    public void visitI64And(I64And i64And) throws IOException {
        output.write(Opcode.I64_AND.opcode());
    }

    @Override
    public void visitI64DivSigned(I64DivSigned i64DivSigned) throws IOException {
        output.write(Opcode.I64_DIV_S.opcode());
    }

    @Override
    public void visitI64DivUnsigned(I64DivUnsigned i64DivUnsigned) throws IOException {
        output.write(Opcode.I64_DIV_U.opcode());
    }

    @Override
    public void visitI64Mul(I64Mul i64Mul) throws IOException {
        output.write(Opcode.I64_MUL.opcode());
    }

    @Override
    public void visitI64Or(I64Or i64Or) throws IOException {
        output.write(Opcode.I64_OR.opcode());
    }

    @Override
    public void visitI64RemSigned(I64RemSigned i64RemSigned) throws IOException {
        output.write(Opcode.I64_REM_S.opcode());
    }

    @Override
    public void visitI64RemUnsigned(I64RemUnsigned i64RemUnsigned) throws IOException {
        output.write(Opcode.I64_REM_U.opcode());
    }

    @Override
    public void visitI64Rotl(I64Rotl i64Rotl) throws IOException {
        output.write(Opcode.I64_ROTL.opcode());
    }

    @Override
    public void visitI64Rotr(I64Rotr i64Rotr) throws IOException {
        output.write(Opcode.I64_ROTR.opcode());
    }

    @Override
    public void visitI64Shl(I64Shl i64Shl) throws IOException {
        output.write(Opcode.I64_SHL.opcode());
    }

    @Override
    public void visitI64ShrSigned(I64ShrSigned i64ShrSigned) throws IOException {
        output.write(Opcode.I64_SHR_S.opcode());
    }

    @Override
    public void visitI64ShrUnsigned(I64ShrUnsigned i64ShrUnsigned) throws IOException {
        output.write(Opcode.I64_SHR_U.opcode());
    }

    @Override
    public void visitI64Sub(I64Sub i64Sub) throws IOException {
        output.write(Opcode.I64_SUB.opcode());
    }

    @Override
    public void visitI64Xor(I64Xor i64Xor) throws IOException {
        output.write(Opcode.I64_XOR.opcode());
    }

    @Override
    public void visitF32Add(F32Add f32Add) throws IOException {
        output.write(Opcode.F32_ADD.opcode());
    }

    @Override
    public void visitF32Copysign(F32Copysign f32Copysign) throws IOException {
        output.write(Opcode.F32_COPYSIGN.opcode());
    }

    @Override
    public void visitF32Div(F32Div f32Div) throws IOException {
        output.write(Opcode.F32_DIV.opcode());
    }

    @Override
    public void visitF32Max(F32Max f32Max) throws IOException {
        output.write(Opcode.F32_MAX.opcode());
    }

    @Override
    public void visitF32Min(F32Min f32Min) throws IOException {
        output.write(Opcode.F32_MIN.opcode());
    }

    @Override
    public void visitF32Mul(F32Mul f32Mul) throws IOException {
        output.write(Opcode.F32_MUL.opcode());
    }

    @Override
    public void visitF32Sub(F32Sub f32Sub) throws IOException {
        output.write(Opcode.F32_SUB.opcode());
    }

    @Override
    public void visitF64Add(F64Add f64Add) throws IOException {
        output.write(Opcode.F64_ADD.opcode());
    }

    @Override
    public void visitF64Copysign(F64Copysign f64Copysign) throws IOException {
        output.write(Opcode.F64_COPYSIGN.opcode());
    }

    @Override
    public void visitF64Div(F64Div f64Div) throws IOException {
        output.write(Opcode.F64_DIV.opcode());
    }

    @Override
    public void visitF64Max(F64Max f64Max) throws IOException {
        output.write(Opcode.F64_MAX.opcode());
    }

    @Override
    public void visitF64Min(F64Min f64Min) throws IOException {
        output.write(Opcode.F64_MIN.opcode());
    }

    @Override
    public void visitF64Mul(F64Mul f64Mul) throws IOException {
        output.write(Opcode.F64_MUL.opcode());
    }

    @Override
    public void visitF64Sub(F64Sub f64Sub) throws IOException {
        output.write(Opcode.F64_SUB.opcode());
    }

    @Override
    public void visitI32Eqz(I32Eqz i32Eqz) throws IOException {
        output.write(Opcode.I32_EQZ.opcode());
    }

    @Override
    public void visitI64Eqz(I64Eqz i64Eqz) throws IOException {
        output.write(Opcode.I64_EQZ.opcode());
    }

    @Override
    public void visitI32Eq(I32Eq i32Eq) throws IOException {
        output.write(Opcode.I32_EQ.opcode());
    }

    @Override
    public void visitI32GeSigned(I32GeSigned i32GeSigned) throws IOException {
        output.write(Opcode.I32_GE_S.opcode());
    }

    @Override
    public void visitI32GeUnsigned(I32GeUnsigned i32GeUnsigned) throws IOException {
        output.write(Opcode.I32_GE_U.opcode());
    }

    @Override
    public void visitI32GtSigned(I32GtSigned i32GtSigned) throws IOException {
        output.write(Opcode.I32_GT_S.opcode());
    }

    @Override
    public void visitI32GtUnsigned(I32GtUnsigned i32GtUnsigned) throws IOException {
        output.write(Opcode.I32_GT_U.opcode());
    }

    @Override
    public void visitI32LeSigned(I32LeSigned i32LeSigned) throws IOException {
        output.write(Opcode.I32_LE_S.opcode());
    }

    @Override
    public void visitI32LeUnsigned(I32LeUnsigned i32LeUnsigned) throws IOException {
        output.write(Opcode.I32_LE_U.opcode());
    }

    @Override
    public void visitI32LtSigned(I32LtSigned i32LtSigned) throws IOException {
        output.write(Opcode.I32_LT_S.opcode());
    }

    @Override
    public void visitI32LtUnsigned(I32LtUnsigned i32LtUnsigned) throws IOException {
        output.write(Opcode.I32_LT_U.opcode());
    }

    @Override
    public void visitI32Ne(I32Ne i32Ne) throws IOException {
        output.write(Opcode.I32_NE.opcode());
    }

    @Override
    public void visitI64Eq(I64Eq i64Eq) throws IOException {
        output.write(Opcode.I64_EQ.opcode());
    }

    @Override
    public void visitI64GeSigned(I64GeSigned i64GeSigned) throws IOException {
        output.write(Opcode.I64_GE_S.opcode());
    }

    @Override
    public void visitI64GeUnsigned(I64GeUnsigned i64GeUnsigned) throws IOException {
        output.write(Opcode.I64_GE_U.opcode());
    }

    @Override
    public void visitI64GtSigned(I64GtSigned i64GtSigned) throws IOException {
        output.write(Opcode.I64_GT_S.opcode());
    }

    @Override
    public void visitI64GtUnsigned(I64GtUnsigned i64GtUnsigned) throws IOException {
        output.write(Opcode.I64_GT_U.opcode());
    }

    @Override
    public void visitI64LeSigned(I64LeSigned i64LeSigned) throws IOException {
        output.write(Opcode.I64_LE_S.opcode());
    }

    @Override
    public void visitI64LeUnsigned(I64LeUnsigned i64LeUnsigned) throws IOException {
        output.write(Opcode.I64_LE_U.opcode());
    }

    @Override
    public void visitI64LtSigned(I64LtSigned i64LtSigned) throws IOException {
        output.write(Opcode.I64_LT_S.opcode());
    }

    @Override
    public void visitI64LtUnsigned(I64LtUnsigned i64LtUnsigned) throws IOException {
        output.write(Opcode.I64_LT_U.opcode());
    }

    @Override
    public void visitI64Ne(I64Ne i64Ne) throws IOException {
        output.write(Opcode.I64_NE.opcode());
    }

    @Override
    public void visitF32Eq(F32Eq f32Eq) throws IOException {
        output.write(Opcode.F32_EQ.opcode());
    }

    @Override
    public void visitF32Ge(F32Ge f32Ge) throws IOException {
        output.write(Opcode.F32_GE.opcode());
    }

    @Override
    public void visitF32Gt(F32Gt f32Gt) throws IOException {
        output.write(Opcode.F32_GT.opcode());
    }

    @Override
    public void visitF32Le(F32Le f32Le) throws IOException {
        output.write(Opcode.F32_LE.opcode());
    }

    @Override
    public void visitF32Lt(F32Lt f32Lt) throws IOException {
        output.write(Opcode.F32_LT.opcode());
    }

    @Override
    public void visitF32Ne(F32Ne f32Ne) throws IOException {
        output.write(Opcode.F32_NE.opcode());
    }

    @Override
    public void visitF64Eq(F64Eq f64Eq) throws IOException {
        output.write(Opcode.F64_EQ.opcode());
    }

    @Override
    public void visitF64Ge(F64Ge f64Ge) throws IOException {
        output.write(Opcode.F64_GE.opcode());
    }

    @Override
    public void visitF64Gt(F64Gt f64Gt) throws IOException {
        output.write(Opcode.F64_GT.opcode());
    }

    @Override
    public void visitF64Le(F64Le f64Le) throws IOException {
        output.write(Opcode.F64_LE.opcode());
    }

    @Override
    public void visitF64Lt(F64Lt f64Lt) throws IOException {
        output.write(Opcode.F64_LT.opcode());
    }

    @Override
    public void visitF64Ne(F64Ne f64Ne) throws IOException {
        output.write(Opcode.F64_NE.opcode());
    }

    @Override
    public void visitI32Extend8Signed(I32Extend8Signed i32Extend8Signed) throws IOException {
        output.write(Opcode.I32_EXTEND8_S.opcode());
    }

    @Override
    public void visitI64Extend8Signed(I64Extend8Signed i64Extend8Signed) throws IOException {
        output.write(Opcode.I64_EXTEND8_S.opcode());
    }

    @Override
    public void visitI32Extend16Signed(I32Extend16Signed i32Extend16Signed) throws IOException {
        output.write(Opcode.I32_EXTEND16_S.opcode());
    }

    @Override
    public void visitI64Extend16Signed(I64Extend16Signed i64Extend16Signed) throws IOException {
        output.write(Opcode.I64_EXTEND16_S.opcode());
    }

    @Override
    public void visitI64Extend32Signed(I64Extend32Signed i64Extend32Signed) throws IOException {
        output.write(Opcode.I64_EXTEND32_S.opcode());
    }

    @Override
    public void visitI32WrapI64(I32WrapI64 i32WrapI64) throws IOException {
        output.write(Opcode.I32_WRAP_I64.opcode());
    }

    @Override
    public void visitI64ExtendI32Signed(I64ExtendI32Signed i64ExtendI32Signed) throws IOException {
        output.write(Opcode.I64_EXTEND_I32_S.opcode());
    }

    @Override
    public void visitI64ExtendI32Unsigned(I64ExtendI32Unsigned i64ExtendI32Unsigned) throws IOException {
        output.write(Opcode.I64_EXTEND_I32_U.opcode());
    }

    @Override
    public void visitI32TruncF32Signed(I32TruncF32Signed i32TruncF32Signed) throws IOException {
        output.write(Opcode.I32_TRUNC_F32_S.opcode());
    }

    @Override
    public void visitI32TruncF32Unsigned(I32TruncF32Unsigned i32TruncF32Unsigned) throws IOException {
        output.write(Opcode.I32_TRUNC_F32_U.opcode());
    }

    @Override
    public void visitI32TruncF64Signed(I32TruncF64Signed i32TruncF64Signed) throws IOException {
        output.write(Opcode.I32_TRUNC_F64_S.opcode());
    }

    @Override
    public void visitI32TruncF64Unsigned(I32TruncF64Unsigned i32TruncF64Unsigned) throws IOException {
        output.write(Opcode.I32_TRUNC_F64_U.opcode());
    }

    @Override
    public void visitI64TruncF32Signed(I64TruncF32Signed i64TruncF32Signed) throws IOException {
        output.write(Opcode.I64_TRUNC_F32_S.opcode());
    }

    @Override
    public void visitI64TruncF32Unsigned(I64TruncF32Unsigned i64TruncF32Unsigned) throws IOException {
        output.write(Opcode.I64_TRUNC_F32_U.opcode());
    }

    @Override
    public void visitI64TruncF64Signed(I64TruncF64Signed i64TruncF64Signed) throws IOException {
        output.write(Opcode.I64_TRUNC_F64_S.opcode());
    }

    @Override
    public void visitI64TruncF64Unsigned(I64TruncF64Unsigned i64TruncF64Unsigned) throws IOException {
        output.write(Opcode.I64_TRUNC_F64_U.opcode());
    }

    @Override
    public void visitI32TruncSatF32Signed(I32TruncSatF32Signed i32TruncSatF32Signed) throws IOException {
        output.write(Opcode.I32_TRUNC_SAT_F32_S.bytes());
    }

    @Override
    public void visitI32TruncSatF32Unsigned(I32TruncSatF32Unsigned i32TruncSatF32Unsigned) throws IOException {
        output.write(Opcode.I32_TRUNC_SAT_F32_U.bytes());
    }

    @Override
    public void visitI32TruncSatF64Signed(I32TruncSatF64Signed i32TruncSatF64Signed) throws IOException {
        output.write(Opcode.I32_TRUNC_SAT_F64_S.bytes());
    }

    @Override
    public void visitI32TruncSatF64Unsigned(I32TruncSatF64Unsigned i32TruncSatF64Unsigned) throws IOException {
        output.write(Opcode.I32_TRUNC_SAT_F64_U.bytes());
    }

    @Override
    public void visitI64TruncSatF32Signed(I64TruncSatF32Signed i64TruncSatF32Signed) throws IOException {
        output.write(Opcode.I64_TRUNC_SAT_F32_S.bytes());
    }

    @Override
    public void visitI64TruncSatF32Unsigned(I64TruncSatF32Unsigned i64TruncSatF32Unsigned) throws IOException {
        output.write(Opcode.I64_TRUNC_SAT_F32_U.bytes());
    }

    @Override
    public void visitI64TruncSatF64Signed(I64TruncSatF64Signed i64TruncSatF64Signed) throws IOException {
        output.write(Opcode.I64_TRUNC_SAT_F64_S.bytes());
    }

    @Override
    public void visitI64TruncSatF64Unsigned(I64TruncSatF64Unsigned i64TruncSatF64Unsigned) throws IOException {
        output.write(Opcode.I64_TRUNC_SAT_F64_U.bytes());
    }

    @Override
    public void visitF32DemoteF64(F32DemoteF64 f32DemoteF64) throws IOException {
        output.write(Opcode.F32_DEMOTE_F64.opcode());
    }

    @Override
    public void visitF64PromoteF32(F64PromoteF32 f64PromoteF32) throws IOException {
        output.write(Opcode.F64_PROMOTE_F32.opcode());
    }

    @Override
    public void visitF32ConvertI32Signed(F32ConvertI32Signed f32ConvertI32Signed) throws IOException {
        output.write(Opcode.F32_CONVERT_I32_S.opcode());
    }

    @Override
    public void visitF32ConvertI32Unsigned(F32ConvertI32Unsigned f32ConvertI32Unsigned) throws IOException {
        output.write(Opcode.F32_CONVERT_I32_U.opcode());
    }

    @Override
    public void visitF32ConvertI64Signed(F32ConvertI64Signed f32ConvertI64Signed) throws IOException {
        output.write(Opcode.F32_CONVERT_I64_S.opcode());
    }

    @Override
    public void visitF32ConvertI64Unsigned(F32ConvertI64Unsigned f32ConvertI64Unsigned) throws IOException {
        output.write(Opcode.F32_CONVERT_I64_U.opcode());
    }

    @Override
    public void visitF64ConvertI32Signed(F64ConvertI32Signed f64ConvertI32Signed) throws IOException {
        output.write(Opcode.F64_CONVERT_I32_S.opcode());
    }

    @Override
    public void visitF64ConvertI32Unsigned(F64ConvertI32Unsigned f64ConvertI32Unsigned) throws IOException {
        output.write(Opcode.F64_CONVERT_I32_U.opcode());
    }

    @Override
    public void visitF64ConvertI64Signed(F64ConvertI64Signed f64ConvertI64Signed) throws IOException {
        output.write(Opcode.F64_CONVERT_I64_S.opcode());
    }

    @Override
    public void visitF64ConvertI64Unsigned(F64ConvertI64Unsigned f64ConvertI64Unsigned) throws IOException {
        output.write(Opcode.F64_CONVERT_I64_U.opcode());
    }

    @Override
    public void visitI32ReinterpretF32(I32ReinterpretF32 i32ReinterpretF32) throws IOException {
        output.write(Opcode.I32_REINTERPRET_F32.opcode());
    }

    @Override
    public void visitI64ReinterpretF64(I64ReinterpretF64 i64ReinterpretF64) throws IOException {
        output.write(Opcode.I64_REINTERPRET_F64.opcode());
    }

    @Override
    public void visitF32ReinterpretI32(F32ReinterpretI32 f32ReinterpretI32) throws IOException {
        output.write(Opcode.F32_REINTERPRET_I32.opcode());
    }

    @Override
    public void visitF64ReinterpretI64(F64ReinterpretI64 f64ReinterpretI64) throws IOException {
        output.write(Opcode.F64_REINTERPRET_I64.opcode());
    }

    // Parametric instructions
    @Override
    public void visitDrop(Drop drop) throws IOException {
        output.write(Opcode.DROP.opcode());
    }

    @Override
    public void visitSelect(Select select) throws IOException {
        if (select.valType() != null) {
            output.write(Opcode.SELECT_TYPE.opcode());
            LEB128.writeUnsigned(output, 1);
            select.valType().accept(typeVisitor);
        } else {
            output.write(Opcode.SELECT.opcode());
        }
    }

    // Reference instructions
    @Override
    public void visitRefAsNonNull(RefAsNonNull refAsNonNull) throws IOException {
        output.write(Opcode.REF_AS_NON_NULL.opcode());
    }

    @Override
    public void visitRefIsNull(RefIsNull refIsNull) throws IOException {
        output.write(Opcode.REF_IS_NULL.opcode());
    }

    // Table instructions
    @Override
    public void visitTableGet(TableGet tableGet) throws IOException {
        output.write(Opcode.TABLE_GET.opcode());
        LEB128.writeUnsigned(output, tableGet.tableIndex());
    }

    @Override
    public void visitTableSet(TableSet tableSet) throws IOException {
        output.write(Opcode.TABLE_SET.opcode());
        LEB128.writeUnsigned(output, tableSet.tableIndex());
    }

    @Override
    public void visitTableSize(TableSize tableSize) throws IOException {
        output.write(Opcode.TABLE_SIZE.bytes());
        LEB128.writeUnsigned(output, tableSize.tableIndex());
    }

    @Override
    public void visitTableGrow(TableGrow tableGrow) throws IOException {
        output.write(Opcode.TABLE_GROW.bytes());
        LEB128.writeUnsigned(output, tableGrow.tableIndex());
    }

    @Override
    public void visitTableFill(TableFill tableFill) throws IOException {
        output.write(Opcode.TABLE_FILL.bytes());
        LEB128.writeUnsigned(output, tableFill.tableIndex());
    }

    @Override
    public void visitTableCopy(TableCopy tableCopy) throws IOException {
        output.write(Opcode.TABLE_COPY.bytes());
        LEB128.writeUnsigned(output, tableCopy.targetTableIndex());
        LEB128.writeUnsigned(output, tableCopy.sourceTableIndex());
    }

    @Override
    public void visitTableInit(TableInit tableInit) throws IOException {
        output.write(Opcode.TABLE_INIT.bytes());
        LEB128.writeUnsigned(output, tableInit.elemIndex());
        LEB128.writeUnsigned(output, tableInit.tableIndex());
    }

    @Override
    public void visitElemDrop(ElemDrop elemDrop) throws IOException {
        output.write(Opcode.ELEM_DROP.bytes());
        LEB128.writeUnsigned(output, elemDrop.elemIndex());
    }

    // Variable instructions
    @Override
    public void visitGlobalSet(GlobalSet globalSet) throws IOException {
        output.write(Opcode.GLOBAL_SET.opcode());
        LEB128.writeUnsigned(output, globalSet.globalIndex());
    }

    @Override
    public void visitLocalGet(LocalGet localGet) throws IOException {
        output.write(Opcode.LOCAL_GET.opcode());
        LEB128.writeUnsigned(output, localGet.localIndex());
    }

    @Override
    public void visitLocalSet(LocalSet localSet) throws IOException {
        output.write(Opcode.LOCAL_SET.opcode());
        LEB128.writeUnsigned(output, localSet.localIndex());
    }

    @Override
    public void visitLocalTee(LocalTee localTee) throws IOException {
        output.write(Opcode.LOCAL_TEE.opcode());
        LEB128.writeUnsigned(output, localTee.localIndex());
    }

    @Override
    public void visitExpression(Expression expression) throws IOException {
        for (Instruction instr: expression.instructions()) {
            instr.accept(this);
        }

        output.write(Opcode.END.opcode());
    }
}
