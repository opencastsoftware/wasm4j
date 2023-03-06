package com.opencastsoftware.wasm4j.types;

public interface WasmTypeVisitor<T extends Exception> {

    default void visitType(WasmType type) throws T {
        type.accept(this);
    }

    // Heap types
    default void visitHeapType(HeapType heap) throws T {
        heap.accept(this);
    }

    void visitTypeId(TypeId typeId) throws T;

    void visitHeapFunc(HeapFuncType heapFunc) throws T;

    void visitHeapExtern(HeapExternType heapExtern) throws T;

    // Extern types
    default void visitExternType(ExternType extern) throws T {
        extern.accept(this);
    }

    void visitLimits(Limits limits) throws T;

    void visitFuncType(FuncType func) throws T;

    void visitTableType(TableType table) throws T;

    void visitMemType(MemType mem) throws T;

    void visitGlobalType(GlobalType global) throws T;

    // Value types
    default void visitValType(ValType value) throws T {
        value.accept(this);
    }

    // Number types
    default void visitNumType(NumType num) throws T {
        num.accept(this);
    }

    void visitI32Type(I32Type i32) throws T;

    void visitI64Type(I64Type i64) throws T;

    void visitF32Type(F32Type f32) throws T;

    void visitF64Type(F64Type f64) throws T;

    // Reference types
    void visitRefType(RefType ref) throws T;

    // Vector types
    default void visitVecType(VecType vec) throws T {
        vec.accept(this);
    }

    void visitV128Type(V128Type v128) throws T;
}
