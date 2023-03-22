/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j.instructions.numeric;

import com.opencastsoftware.wasm4j.instructions.Instruction;
import com.opencastsoftware.wasm4j.instructions.InstructionVisitor;
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

public interface NumericInstruction extends Instruction {
    <T extends Exception> void accept(NumericInstructionVisitor<T> visitor) throws T;

    default <T extends Exception> void accept(InstructionVisitor<T> visitor) throws T {
        accept((NumericInstructionVisitor<T>) visitor);
    }

    // Integer constant operations
    static I32Const i32_const(int value) {
        return new I32Const(value);
    }

    static I64Const i64_const(long value) {
        return new I64Const(value);
    }

    // Float constant operations
    static F32Const f32_const(float value) {
        return new F32Const(value);
    }

    static F64Const f64_const(double value) {
        return new F64Const(value);
    }

    // Integer unary operations
    static I32Clz i32_clz() {
        return I32Clz.INSTANCE;
    }

    static I64Clz i64_clz() {
        return I64Clz.INSTANCE;
    }

    static I32Ctz i32_ctz() {
        return I32Ctz.INSTANCE;
    }

    static I64Ctz i64_ctz() {
        return I64Ctz.INSTANCE;
    }

    static I32Popcnt i32_popcnt() {
        return I32Popcnt.INSTANCE;
    }

    static I64Popcnt i64_popcnt() {
        return I64Popcnt.INSTANCE;
    }

    // Float unary operations
    static F32Abs f32_abs() {
        return F32Abs.INSTANCE;
    }

    static F64Abs f64_abs() {
        return F64Abs.INSTANCE;
    }

    static F32Neg f32_neg() {
        return F32Neg.INSTANCE;
    }

    static F64Neg f64_neg() {
        return F64Neg.INSTANCE;
    }

    static F32Sqrt f32_sqrt() {
        return F32Sqrt.INSTANCE;
    }

    static F64Sqrt f64_sqrt() {
        return F64Sqrt.INSTANCE;
    }

    static F32Ceil f32_ceil() {
        return F32Ceil.INSTANCE;
    }

    static F64Ceil f64_ceil() {
        return F64Ceil.INSTANCE;
    }

    static F32Floor f32_floor() {
        return F32Floor.INSTANCE;
    }

    static F64Floor f64_floor() {
        return F64Floor.INSTANCE;
    }

    static F32Trunc f32_trunc() {
        return F32Trunc.INSTANCE;
    }

    static F64Trunc f64_trunc() {
        return F64Trunc.INSTANCE;
    }

    static F32Nearest f32_nearest() {
        return F32Nearest.INSTANCE;
    }

    static F64Nearest f64_nearest() {
        return F64Nearest.INSTANCE;
    }

    // Integer binary operations
    static I32Add i32_add() {
        return I32Add.INSTANCE;
    }

    static I64Add i64_add() {
        return I64Add.INSTANCE;
    }

    static I32Sub i32_sub() {
        return I32Sub.INSTANCE;
    }

    static I64Sub i64_sub() {
        return I64Sub.INSTANCE;
    }

    static I32Mul i32_mul() {
        return I32Mul.INSTANCE;
    }

    static I64Mul i64_mul() {
        return I64Mul.INSTANCE;
    }

    static I32DivSigned i32_div_s() {
        return I32DivSigned.INSTANCE;
    }

    static I64DivSigned i64_div_s() {
        return I64DivSigned.INSTANCE;
    }

    static I32DivUnsigned i32_div_u() {
        return I32DivUnsigned.INSTANCE;
    }

    static I64DivUnsigned i64_div_u() {
        return I64DivUnsigned.INSTANCE;
    }

    static I32RemSigned i32_rem_s() {
        return I32RemSigned.INSTANCE;
    }

    static I64RemSigned i64_rem_s() {
        return I64RemSigned.INSTANCE;
    }

    static I32RemUnsigned i32_rem_u() {
        return I32RemUnsigned.INSTANCE;
    }

    static I64RemUnsigned i64_rem_u() {
        return I64RemUnsigned.INSTANCE;
    }

    static I32And i32_and() {
        return I32And.INSTANCE;
    }

    static I64And i64_and() {
        return I64And.INSTANCE;
    }

    static I32Or i32_or() {
        return I32Or.INSTANCE;
    }

    static I64Or i64_or() {
        return I64Or.INSTANCE;
    }

    static I32Xor i32_xor() {
        return I32Xor.INSTANCE;
    }

    static I64Xor i64_xor() {
        return I64Xor.INSTANCE;
    }

    static I32Shl i32_shl() {
        return I32Shl.INSTANCE;
    }

    static I64Shl i64_shl() {
        return I64Shl.INSTANCE;
    }

    static I32ShrSigned i32_shr_s() {
        return I32ShrSigned.INSTANCE;
    }

    static I64ShrSigned i64_shr_s() {
        return I64ShrSigned.INSTANCE;
    }

    static I32ShrUnsigned i32_shr_u() {
        return I32ShrUnsigned.INSTANCE;
    }

    static I64ShrUnsigned i64_shr_u() {
        return I64ShrUnsigned.INSTANCE;
    }

    static I32Rotl i32_rotl() {
        return I32Rotl.INSTANCE;
    }

    static I64Rotl i64_rotl() {
        return I64Rotl.INSTANCE;
    }

    static I32Rotr i32_rotr() {
        return I32Rotr.INSTANCE;
    }

    static I64Rotr i64_rotr() {
        return I64Rotr.INSTANCE;
    }

    // Float binary operations
    static F32Add f32_add() {
        return F32Add.INSTANCE;
    }

    static F64Add f64_add() {
        return F64Add.INSTANCE;
    }

    static F32Sub f32_sub() {
        return F32Sub.INSTANCE;
    }

    static F64Sub f64_sub() {
        return F64Sub.INSTANCE;
    }

    static F32Mul f32_mul() {
        return F32Mul.INSTANCE;
    }

    static F64Mul f64_mul() {
        return F64Mul.INSTANCE;
    }

    static F32Div f32_div() {
        return F32Div.INSTANCE;
    }

    static F64Div f64_div() {
        return F64Div.INSTANCE;
    }

    static F32Min f32_min() {
        return F32Min.INSTANCE;
    }

    static F64Min f64_min() {
        return F64Min.INSTANCE;
    }

    static F32Max f32_max() {
        return F32Max.INSTANCE;
    }

    static F64Max f64_max() {
        return F64Max.INSTANCE;
    }

    static F32Copysign f32_copysign() {
        return F32Copysign.INSTANCE;
    }

    static F64Copysign f64_copysign() {
        return F64Copysign.INSTANCE;
    }

    // Integer test operations
    static I32Eqz i32_eqz() {
        return I32Eqz.INSTANCE;
    }

    static I64Eqz i64_eqz() {
        return I64Eqz.INSTANCE;
    }

    // Integer relational operations
    static I32Eq i32_eq() {
        return I32Eq.INSTANCE;
    }

    static I64Eq i64_eq() {
        return I64Eq.INSTANCE;
    }

    static I32Ne i32_ne() {
        return I32Ne.INSTANCE;
    }

    static I64Ne i64_ne() {
        return I64Ne.INSTANCE;
    }

    static I32LtSigned i32_lt_s() {
        return I32LtSigned.INSTANCE;
    }

    static I64LtSigned i64_lt_s() {
        return I64LtSigned.INSTANCE;
    }

    static I32LtUnsigned i32_lt_u() {
        return I32LtUnsigned.INSTANCE;
    }

    static I64LtUnsigned i64_lt_u() {
        return I64LtUnsigned.INSTANCE;
    }

    static I32GtSigned i32_gt_s() {
        return I32GtSigned.INSTANCE;
    }

    static I64GtSigned i64_gt_s() {
        return I64GtSigned.INSTANCE;
    }

    static I32GtUnsigned i32_gt_u() {
        return I32GtUnsigned.INSTANCE;
    }

    static I64GtUnsigned i64_gt_u() {
        return I64GtUnsigned.INSTANCE;
    }

    static I32LeSigned i32_le_s() {
        return I32LeSigned.INSTANCE;
    }

    static I64LeSigned i64_le_s() {
        return I64LeSigned.INSTANCE;
    }

    static I32LeUnsigned i32_le_u() {
        return I32LeUnsigned.INSTANCE;
    }

    static I64LeUnsigned i64_le_u() {
        return I64LeUnsigned.INSTANCE;
    }

    static I32GeSigned i32_ge_s() {
        return I32GeSigned.INSTANCE;
    }

    static I64GeSigned i64_ge_s() {
        return I64GeSigned.INSTANCE;
    }

    static I32GeUnsigned i32_ge_u() {
        return I32GeUnsigned.INSTANCE;
    }

    static I64GeUnsigned i64_ge_u() {
        return I64GeUnsigned.INSTANCE;
    }

    // Float relational operations
    static F32Eq f32_eq() {
        return F32Eq.INSTANCE;
    }

    static F64Eq f64_eq() {
        return F64Eq.INSTANCE;
    }

    static F32Ne f32_ne() {
        return F32Ne.INSTANCE;
    }

    static F64Ne f64_ne() {
        return F64Ne.INSTANCE;
    }

    static F32Lt f32_lt() {
        return F32Lt.INSTANCE;
    }

    static F64Lt f64_lt() {
        return F64Lt.INSTANCE;
    }

    static F32Gt f32_gt() {
        return F32Gt.INSTANCE;
    }

    static F64Gt f64_gt() {
        return F64Gt.INSTANCE;
    }

    static F32Le f32_le() {
        return F32Le.INSTANCE;
    }

    static F64Le f64_le() {
        return F64Le.INSTANCE;
    }

    static F32Ge f32_ge() {
        return F32Ge.INSTANCE;
    }

    static F64Ge f64_ge() {
        return F64Ge.INSTANCE;
    }

    // Integer conversion operations
    static I32Extend8Signed i32_extend8_s() {
        return I32Extend8Signed.INSTANCE;
    }

    static I64Extend8Signed i64_extend8_s() {
        return I64Extend8Signed.INSTANCE;
    }

    static I32Extend16Signed i32_extend16_s() {
        return I32Extend16Signed.INSTANCE;
    }

    static I64Extend16Signed i64_extend16_s() {
        return I64Extend16Signed.INSTANCE;
    }

    static I64Extend32Signed i64_extend32_s() {
        return I64Extend32Signed.INSTANCE;
    }

    static I32WrapI64 i32_wrap_i64() {
        return I32WrapI64.INSTANCE;
    }

    static I64ExtendI32Signed i64_extend_i32_s() {
        return I64ExtendI32Signed.INSTANCE;
    }

    static I64ExtendI32Unsigned i64_extend_i32_u() {
        return I64ExtendI32Unsigned.INSTANCE;
    }

    static I32TruncF32Signed i32_trunc_f32_s() {
        return I32TruncF32Signed.INSTANCE;
    }

    static I32TruncF32Unsigned i32_trunc_f32_u() {
        return I32TruncF32Unsigned.INSTANCE;
    }

    static I32TruncF64Signed i32_trunc_f64_s() {
        return I32TruncF64Signed.INSTANCE;
    }

    static I32TruncF64Unsigned i32_trunc_f64_u() {
        return I32TruncF64Unsigned.INSTANCE;
    }

    static I64TruncF32Signed i64_trunc_f32_s() {
        return I64TruncF32Signed.INSTANCE;
    }

    static I64TruncF32Unsigned i64_trunc_f32_u() {
        return I64TruncF32Unsigned.INSTANCE;
    }

    static I64TruncF64Signed i64_trunc_f64_s() {
        return I64TruncF64Signed.INSTANCE;
    }

    static I64TruncF64Unsigned i64_trunc_f64_u() {
        return I64TruncF64Unsigned.INSTANCE;
    }

    static I32TruncSatF32Signed i32_trunc_sat_f32_s() {
        return I32TruncSatF32Signed.INSTANCE;
    }

    static I32TruncSatF32Unsigned i32_trunc_sat_f32_u() {
        return I32TruncSatF32Unsigned.INSTANCE;
    }

    static I32TruncSatF64Signed i32_trunc_sat_f64_s() {
        return I32TruncSatF64Signed.INSTANCE;
    }

    static I32TruncSatF64Unsigned i32_trunc_sat_f64_u() {
        return I32TruncSatF64Unsigned.INSTANCE;
    }

    static I64TruncSatF32Signed i64_trunc_sat_f32_s() {
        return I64TruncSatF32Signed.INSTANCE;
    }

    static I64TruncSatF32Unsigned i64_trunc_sat_f32_u() {
        return I64TruncSatF32Unsigned.INSTANCE;
    }

    static I64TruncSatF64Signed i64_trunc_sat_f64_s() {
        return I64TruncSatF64Signed.INSTANCE;
    }

    static I64TruncSatF64Unsigned i64_trunc_sat_f64_u() {
        return I64TruncSatF64Unsigned.INSTANCE;
    }

    static F32DemoteF64 f32_demote_f64() {
        return F32DemoteF64.INSTANCE;
    }

    static F64PromoteF32 f64_promote_f32() {
        return F64PromoteF32.INSTANCE;
    }

    static F32ConvertI32Signed f32_convert_i32_s() {
        return F32ConvertI32Signed.INSTANCE;
    }

    static F32ConvertI32Unsigned f32_convert_i32_u() {
        return F32ConvertI32Unsigned.INSTANCE;
    }

    static F32ConvertI64Signed f32_convert_i64_s() {
        return F32ConvertI64Signed.INSTANCE;
    }

    static F32ConvertI64Unsigned f32_convert_i64_u() {
        return F32ConvertI64Unsigned.INSTANCE;
    }

    static F64ConvertI32Signed f64_convert_i32_s() {
        return F64ConvertI32Signed.INSTANCE;
    }

    static F64ConvertI32Unsigned f64_convert_i32_u() {
        return F64ConvertI32Unsigned.INSTANCE;
    }

    static F64ConvertI64Signed f64_convert_i64_s() {
        return F64ConvertI64Signed.INSTANCE;
    }

    static F64ConvertI64Unsigned f64_convert_i64_u() {
        return F64ConvertI64Unsigned.INSTANCE;
    }

    static I32ReinterpretF32 i32_reinterpret_f32() {
        return I32ReinterpretF32.INSTANCE;
    }

    static I64ReinterpretF64 i64_reinterpret_f64() {
        return I64ReinterpretF64.INSTANCE;
    }

    static F32ReinterpretI32 f32_reinterpret_i32() {
        return F32ReinterpretI32.INSTANCE;
    }

    static F64ReinterpretI64 f64_reinterpret_i64() {
        return F64ReinterpretI64.INSTANCE;
    }
}
