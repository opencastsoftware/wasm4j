package com.opencastsoftware.wasm4j.instructions.numeric;

import com.opencastsoftware.wasm4j.instructions.numeric.floating.F32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.F64Const;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.binary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.relational.*;
import com.opencastsoftware.wasm4j.instructions.numeric.floating.unary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I32Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.I64Const;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.binary.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.conversion.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.relational.*;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.test.I32Eqz;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.test.I64Eqz;
import com.opencastsoftware.wasm4j.instructions.numeric.integer.unary.*;

public interface NumericInstructionVisitor<T extends Exception> {
    default void visitNumericInstruction(NumericInstruction numeric) throws T {
        numeric.accept(this);
    }

    // Integer constant operations
    void visitI32Const(I32Const i32Const) throws T;

    void visitI64Const(I64Const i64Const) throws T;

    // Float constant operations
    void visitF32Const(F32Const f32Const) throws T;

    void visitF64Const(F64Const f64Const) throws T;

    // Integer unary operations
    void visitI32Clz(I32Clz i32Clz) throws T;

    void visitI32Ctz(I32Ctz i32Ctz) throws T;

    void visitI32Popcnt(I32Popcnt i32Popcnt) throws T;

    void visitI64Clz(I64Clz i64Clz) throws T;

    void visitI64Ctz(I64Ctz i64Ctz) throws T;

    void visitI64Popcnt(I64Popcnt i64Popcnt) throws T;

    // Float unary operations
    void visitF32Abs(F32Abs f32Abs) throws T;

    void visitF32Ceil(F32Ceil f32Ceil) throws T;

    void visitF32Floor(F32Floor f32Floor) throws T;

    void visitF32Nearest(F32Nearest f32Nearest) throws T;

    void visitF32Neg(F32Neg f32Neg) throws T;

    void visitF32Sqrt(F32Sqrt f32Sqrt) throws T;

    void visitF32Trunc(F32Trunc f32Trunc) throws T;

    void visitF64Abs(F64Abs f64Abs) throws T;

    void visitF64Ceil(F64Ceil f64Ceil) throws T;

    void visitF64Floor(F64Floor f64Floor) throws T;

    void visitF64Nearest(F64Nearest f64Nearest) throws T;

    void visitF64Neg(F64Neg f64Neg) throws T;

    void visitF64Sqrt(F64Sqrt f64Sqrt) throws T;

    void visitF64Trunc(F64Trunc f64Trunc) throws T;

    // Integer binary operations
    void visitI32Add(I32Add i32Add) throws T;

    void visitI32And(I32And i32And) throws T;

    void visitI32DivSigned(I32DivSigned i32DivSigned) throws T;

    void visitI32DivUnsigned(I32DivUnsigned i32DivUnsigned) throws T;

    void visitI32Mul(I32Mul i32Mul) throws T;

    void visitI32Or(I32Or i32Or) throws T;

    void visitI32RemSigned(I32RemSigned i32RemSigned) throws T;

    void visitI32RemUnsigned(I32RemUnsigned i32RemUnsigned) throws T;

    void visitI32Rotl(I32Rotl i32Rotl) throws T;

    void visitI32Rotr(I32Rotr i32Rotr) throws T;

    void visitI32Shl(I32Shl i32Shl) throws T;

    void visitI32ShrSigned(I32ShrSigned i32ShrSigned) throws T;

    void visitI32ShrUnsigned(I32ShrUnsigned i32ShrUnsigned) throws T;

    void visitI32Sub(I32Sub i32Sub) throws T;

    void visitI32Xor(I32Xor i32Xor) throws T;

    void visitI64Add(I64Add i64Add) throws T;

    void visitI64And(I64And i64And) throws T;

    void visitI64DivSigned(I64DivSigned i64DivSigned) throws T;

    void visitI64DivUnsigned(I64DivUnsigned i64DivUnsigned) throws T;

    void visitI64Mul(I64Mul i64Mul) throws T;

    void visitI64Or(I64Or i64Or) throws T;

    void visitI64RemSigned(I64RemSigned i64RemSigned) throws T;

    void visitI64RemUnsigned(I64RemUnsigned i64RemUnsigned) throws T;

    void visitI64Rotl(I64Rotl i64Rotl) throws T;

    void visitI64Rotr(I64Rotr i64Rotr) throws T;

    void visitI64Shl(I64Shl i64Shl) throws T;

    void visitI64ShrSigned(I64ShrSigned i64ShrSigned) throws T;

    void visitI64ShrUnsigned(I64ShrUnsigned i64ShrUnsigned) throws T;

    void visitI64Sub(I64Sub i64Sub) throws T;

    void visitI64Xor(I64Xor i64Xor) throws T;

    // Float binary operations
    void visitF32Add(F32Add f32Add) throws T;

    void visitF32Copysign(F32Copysign f32Copysign) throws T;

    void visitF32Div(F32Div f32Div) throws T;

    void visitF32Max(F32Max f32Max) throws T;

    void visitF32Min(F32Min f32Min) throws T;

    void visitF32Mul(F32Mul f32Mul) throws T;

    void visitF32Sub(F32Sub f32Sub) throws T;

    void visitF64Add(F64Add f64Add) throws T;

    void visitF64Copysign(F64Copysign f64Copysign) throws T;

    void visitF64Div(F64Div f64Div) throws T;

    void visitF64Max(F64Max f64Max) throws T;

    void visitF64Min(F64Min f64Min) throws T;

    void visitF64Mul(F64Mul f64Mul) throws T;

    void visitF64Sub(F64Sub f64Sub) throws T;

    // Integer test operations
    void visitI32Eqz(I32Eqz i32Eqz) throws T;

    void visitI64Eqz(I64Eqz i64Eqz) throws T;

    // Integer relational operations
    void visitI32Eq(I32Eq i32Eq) throws T;

    void visitI32GeSigned(I32GeSigned i32GeSigned) throws T;

    void visitI32GeUnsigned(I32GeUnsigned i32GeUnsigned) throws T;

    void visitI32GtSigned(I32GtSigned i32GtSigned) throws T;

    void visitI32GtUnsigned(I32GtUnsigned i32GtUnsigned) throws T;

    void visitI32LeSigned(I32LeSigned i32LeSigned) throws T;

    void visitI32LeUnsigned(I32LeUnsigned i32LeUnsigned) throws T;

    void visitI32LtSigned(I32LtSigned i32LtSigned) throws T;

    void visitI32LtUnsigned(I32LtUnsigned i32LtUnsigned) throws T;

    void visitI32Ne(I32Ne i32Ne) throws T;

    void visitI64Eq(I64Eq i64Eq) throws T;

    void visitI64GeSigned(I64GeSigned i64GeSigned) throws T;

    void visitI64GeUnsigned(I64GeUnsigned i64GeUnsigned) throws T;

    void visitI64GtSigned(I64GtSigned i64GtSigned) throws T;

    void visitI64GtUnsigned(I64GtUnsigned i64GtUnsigned) throws T;

    void visitI64LeSigned(I64LeSigned i64LeSigned) throws T;

    void visitI64LeUnsigned(I64LeUnsigned i64LeUnsigned) throws T;

    void visitI64LtSigned(I64LtSigned i64LtSigned) throws T;

    void visitI64LtUnsigned(I64LtUnsigned i64LtUnsigned) throws T;

    void visitI64Ne(I64Ne i64Ne) throws T;

    // Float relational operations
    void visitF32Eq(F32Eq f32Eq) throws T;

    void visitF32Ge(F32Ge f32Ge) throws T;

    void visitF32Gt(F32Gt f32Gt) throws T;

    void visitF32Le(F32Le f32Le) throws T;

    void visitF32Lt(F32Lt f32Lt) throws T;

    void visitF32Ne(F32Ne f32Ne) throws T;

    void visitF64Eq(F64Eq f64Eq) throws T;

    void visitF64Ge(F64Ge f64Ge) throws T;

    void visitF64Gt(F64Gt f64Gt) throws T;

    void visitF64Le(F64Le f64Le) throws T;

    void visitF64Lt(F64Lt f64Lt) throws T;

    void visitF64Ne(F64Ne f64Ne) throws T;

    // Integer conversion operations
    void visitI32Extend8Signed(I32Extend8Signed i32Extend8Signed) throws T;

    void visitI64Extend8Signed(I64Extend8Signed i64Extend8Signed) throws T;

    void visitI32Extend16Signed(I32Extend16Signed i32Extend16Signed) throws T;

    void visitI64Extend16Signed(I64Extend16Signed i64Extend16Signed) throws T;

    void visitI64Extend32Signed(I64Extend32Signed i64Extend32Signed) throws T;

    void visitI32WrapI64(I32WrapI64 i32WrapI64) throws T;

    void visitI64ExtendI32Signed(I64ExtendI32Signed i64ExtendI32Signed) throws T;

    void visitI64ExtendI32Unsigned(I64ExtendI32Unsigned i64ExtendI32Unsigned) throws T;

    void visitI32TruncF32Signed(I32TruncF32Signed i32TruncF32Signed) throws T;

    void visitI32TruncF32Unsigned(I32TruncF32Unsigned i32TruncF32Unsigned) throws T;

    void visitI32TruncF64Signed(I32TruncF64Signed i32TruncF64Signed) throws T;

    void visitI32TruncF64Unsigned(I32TruncF64Unsigned i32TruncF64Unsigned) throws T;

    void visitI64TruncF32Signed(I64TruncF32Signed i64TruncF32Signed) throws T;

    void visitI64TruncF32Unsigned(I64TruncF32Unsigned i64TruncF32Unsigned) throws T;

    void visitI64TruncF64Signed(I64TruncF64Signed i64TruncF64Signed) throws T;

    void visitI64TruncF64Unsigned(I64TruncF64Unsigned i64TruncF64Unsigned) throws T;

    void visitI32TruncSatF32Signed(I32TruncSatF32Signed i32TruncSatF32Signed) throws T;

    void visitI32TruncSatF32Unsigned(I32TruncSatF32Unsigned i32TruncSatF32Unsigned) throws T;

    void visitI32TruncSatF64Signed(I32TruncSatF64Signed i32TruncSatF64Signed) throws T;

    void visitI32TruncSatF64Unsigned(I32TruncSatF64Unsigned i32TruncSatF64Unsigned) throws T;

    void visitI64TruncSatF32Signed(I64TruncSatF32Signed i64TruncSatF32Signed) throws T;

    void visitI64TruncSatF32Unsigned(I64TruncSatF32Unsigned i64TruncSatF32Unsigned) throws T;

    void visitI64TruncSatF64Signed(I64TruncSatF64Signed i64TruncSatF64Signed) throws T;

    void visitI64TruncSatF64Unsigned(I64TruncSatF64Unsigned i64TruncSatF64Unsigned) throws T;

    void visitF32DemoteF64(F32DemoteF64 f32DemoteF64) throws T;

    void visitF64PromoteF32(F64PromoteF32 f64PromoteF32) throws T;

    void visitF32ConvertI32Signed(F32ConvertI32Signed f32ConvertI32Signed) throws T;

    void visitF32ConvertI32Unsigned(F32ConvertI32Unsigned f32ConvertI32Unsigned) throws T;

    void visitF32ConvertI64Signed(F32ConvertI64Signed f32ConvertI64Signed) throws T;

    void visitF32ConvertI64Unsigned(F32ConvertI64Unsigned f32ConvertI64Unsigned) throws T;

    void visitF64ConvertI32Signed(F64ConvertI32Signed f64ConvertI32Signed) throws T;

    void visitF64ConvertI32Unsigned(F64ConvertI32Unsigned f64ConvertI32Unsigned) throws T;

    void visitF64ConvertI64Signed(F64ConvertI64Signed f64ConvertI64Signed) throws T;

    void visitF64ConvertI64Unsigned(F64ConvertI64Unsigned f64ConvertI64Unsigned) throws T;

    void visitI32ReinterpretF32(I32ReinterpretF32 i32ReinterpretF32) throws T;

    void visitI64ReinterpretF64(I64ReinterpretF64 i64ReinterpretF64) throws T;

    void visitF32ReinterpretI32(F32ReinterpretI32 f32ReinterpretI32) throws T;

    void visitF64ReinterpretI64(F64ReinterpretI64 f64ReinterpretI64) throws T;
}
