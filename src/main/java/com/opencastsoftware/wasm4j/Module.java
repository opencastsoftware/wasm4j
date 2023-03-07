package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private final List<FuncType> types;
    private final List<Func> funcs;
    private final List<Table> tables;
    private final List<MemType> mems;
    private final List<Global> globals;
    private final List<Elem> elems;
    private final List<Data> datas;
    @Nullable
    private final Integer start;
    private final List<Import> imports;
    private final List<Export> exports;

    public Module(List<FuncType> types, List<Func> funcs, List<Table> tables, List<MemType> mems, List<Global> globals, List<Elem> elems, List<Data> datas, @Nullable Integer start, List<Import> imports, List<Export> exports) {
        this.types = types;
        this.funcs = funcs;
        this.tables = tables;
        this.mems = mems;
        this.globals = globals;
        this.elems = elems;
        this.datas = datas;
        this.start = start;
        this.imports = imports;
        this.exports = exports;
    }

    private Module() {
        this.types = new ArrayList<>();
        this.funcs = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.mems = new ArrayList<>();
        this.globals = new ArrayList<>();
        this.elems = new ArrayList<>();
        this.datas = new ArrayList<>();
        this.start = null;
        this.imports = new ArrayList<>();
        this.exports = new ArrayList<>();
    }

    public static Module empty() {
        return new Module();
    }

    public List<FuncType> types() {
        return types;
    }

    public List<Func> funcs() {
        return funcs;
    }

    public List<Table> tables() {
        return tables;
    }

    public List<MemType> mems() {
        return mems;
    }

    public List<Global> globals() {
        return globals;
    }

    public List<Elem> elems() {
        return elems;
    }

    public List<Data> datas() {
        return datas;
    }

    @Nullable
    public Integer start() {
        return start;
    }

    public List<Import> imports() {
        return imports;
    }

    public List<Export> exports() {
        return exports;
    }
}
