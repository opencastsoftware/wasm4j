/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
package com.opencastsoftware.wasm4j;

public class Export {
    private final String name;
    private final Descriptor descriptor;

    public Export(String name, Descriptor descriptor) {
        this.name = name;
        this.descriptor = descriptor;
    }

    public String name() { return name; }
    public Descriptor descriptor() { return descriptor; }

    public static class Descriptor {
        private final int index;
        private final IndexType indexType;

        public static Descriptor func(int index) {
            return new Descriptor(index, IndexType.FUNC);
        }

        public static Descriptor table(int index) {
            return new Descriptor(index, IndexType.TABLE);
        }

        public static Descriptor mem(int index) {
            return new Descriptor(index, IndexType.MEM);
        }

        public static Descriptor global(int index) {
            return new Descriptor(index, IndexType.GLOBAL);
        }

        public Descriptor(int index, IndexType indexType) {
            this.index = index;
            this.indexType = indexType;
        }

        public int index() {
            return index;
        }

        public IndexType indexType() {
            return indexType;
        }
    }
}
