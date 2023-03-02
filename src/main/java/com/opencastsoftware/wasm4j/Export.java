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
        private final long index;
        private final IndexType indexType;

        public static Descriptor func(long index) {
            return new Descriptor(index, IndexType.FUNC);
        }

        public static Descriptor table(long index) {
            return new Descriptor(index, IndexType.TABLE);
        }

        public static Descriptor mem(long index) {
            return new Descriptor(index, IndexType.MEM);
        }

        public static Descriptor global(long index) {
            return new Descriptor(index, IndexType.GLOBAL);
        }

        public Descriptor(long index, IndexType indexType) {
            Preconditions.checkValidU32("index", index);
            this.index = index;
            this.indexType = indexType;
        }

        public long index() {
            return index;
        }

        public IndexType indexType() {
            return indexType;
        }
    }
}
