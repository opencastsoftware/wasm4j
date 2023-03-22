/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.FuncType;
import com.opencastsoftware.wasm4j.types.MemType;
import com.opencastsoftware.wasm4j.types.ValType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
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

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
        private final List<FuncType> types = new ArrayList<>();
        private final List<Func> funcs = new ArrayList<>();
        private final List<Table> tables = new ArrayList<>();
        private final List<MemType> mems = new ArrayList<>();
        private final List<Global> globals = new ArrayList<>();
        private final List<Elem> elems = new ArrayList<>();
        private final List<Data> datas = new ArrayList<>();
        @Nullable
        private Integer start = null;
        private final List<Import> imports = new ArrayList<>();
        private final List<Export> exports = new ArrayList<>();

        public Builder withType(FuncType type) {
            this.types.add(type);

            return this;
        }

        public Builder withFunc(FuncType type, Expression body) {
            withFunc(type, Collections.emptyList(), body);

            return this;
        }

        public Builder withFunc(FuncType type, List<ValType> locals, Expression body) {
            int typeIndex = this.types.size();
            this.types.add(type);
            this.funcs.add(new Func(typeIndex, locals, body));

            return this;
        }

        public Builder withTables(Table... tables) {
            for (Table table : tables) {
                withTable(table);
            }

            return this;
        }

        public Builder withTable(Table table) {
            this.tables.add(table);

            return this;
        }

        public Builder withMemories(MemType... mems) {
            for (MemType mem : mems) {
                withMemory(mem);
            }

            return this;
        }

        public Builder withMemory(MemType mem) {
            this.mems.add(mem);

            return this;
        }

        public Builder withGlobals(Global... globals) {
            for (Global global : globals) {
                withGlobal(global);
            }

            return this;
        }

        public Builder withGlobal(Global global) {
            this.globals.add(global);

            return this;
        }


        public Builder withElems(Elem... elems) {
            for (Elem elem : elems) {
                withElem(elem);
            }

            return this;
        }

        public Builder withElem(Elem elem) {
            this.elems.add(elem);

            return this;
        }

        public Builder withDatas(Data... datas) {
            for (Data data : datas) {
                withData(data);
            }

            return this;
        }

        public Builder withData(Data data) {
            this.datas.add(data);

            return this;
        }

        public Builder withStart(@Nullable Integer start) {
            this.start = start;

            return this;
        }

        public Builder withImports(Import... imports) {
            for (Import imp : imports) {
                withImport(imp);
            }

            return this;
        }

        public Builder withImport(Import imp) {
            this.imports.add(imp);

            return this;
        }

        public Builder withExports(Export... exports) {
            for (Export exp : exports) {
                withExport(exp);
            }

            return this;
        }

        public Builder withExport(Export exp) {
            this.exports.add(exp);

            return this;
        }

        public Module build() {
            return new Module(types, funcs, tables, mems, globals, elems, datas, start, imports, exports);
        }

    }
}
