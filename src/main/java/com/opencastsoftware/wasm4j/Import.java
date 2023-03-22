/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.types.GlobalType;
import com.opencastsoftware.wasm4j.types.MemType;
import com.opencastsoftware.wasm4j.types.TableType;

public class Import {
    private final String module;
    private final String name;
    private final Descriptor descriptor;

    public Import(String module, String name, Descriptor descriptor) {
        this.module = module;
        this.name = name;
        this.descriptor = descriptor;
    }

    public String module() {
        return module;
    }

    public String name() {
        return name;
    }

    public Descriptor descriptor() {
        return descriptor;
    }

    public interface Descriptor {
        static Func func(int funcIndex) {
            return new Func(funcIndex);
        }

        static Table table(TableType tableType) {
            return new Table(tableType);
        }

        static Mem mem(MemType memType) {
            return new Mem(memType);
        }

        static Global global(GlobalType globalType) {
            return new Global(globalType);
        }

        class Func implements Descriptor {
            private final int typeIndex;

            public Func(int funcIndex) {
                this.typeIndex = funcIndex;
            }

            public int typeIndex() {
                return typeIndex;
            }
        }

        class Table implements Descriptor {
            private TableType tableType;

            public Table(TableType tableType) {
                this.tableType = tableType;
            }

            public TableType tableType() {
                return tableType;
            }
        }

        class Mem implements Descriptor {
            private MemType memType;

            public Mem(MemType memType) {
                this.memType = memType;
            }

            public MemType memType() {
                return memType;
            }
        }


        class Global implements Descriptor {
            private GlobalType globalType;

            public Global(GlobalType globalType) {
                this.globalType = globalType;
            }

            public GlobalType globalType() {
                return globalType;
            }
        }
    }
}
