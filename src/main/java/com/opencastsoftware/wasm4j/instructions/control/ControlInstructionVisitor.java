package com.opencastsoftware.wasm4j.instructions.control;

public interface ControlInstructionVisitor<T extends Exception> {
    default void visitControlInstruction(ControlInstruction control) throws T {
        control.accept(this);
    }

    void visitBlock(Block block) throws T;

    void visitBranch(Branch branch) throws T;

    void visitBranchIf(BranchIf branchIf) throws T;

    void visitBranchOnNonNull(BranchOnNonNull branchOnNonNull) throws T;

    void visitBranchOnNull(BranchOnNull branchOnNull) throws T;

    void visitBranchTable(BranchTable branchTable) throws T;

    void visitCall(Call call) throws T;

    void visitCallIndirect(CallIndirect callIndirect) throws T;

    void visitCallRef(CallRef callRef) throws T;

    void visitIf(If ifInstr) throws T;

    void visitLoop(Loop loop) throws T;

    void visitNop(Nop nop) throws T;

    void visitReturn(Return ret) throws T;

    void visitUnreachable(Unreachable unreachable) throws T;
}
