package com.opencastsoftware.wasm4j.types;

import com.opencastsoftware.wasm4j.Preconditions;

import java.util.Optional;

public class Limits {
    private final long min;

    private final Long max;

    public Limits(long min, long max) {
        Preconditions.checkValidU32("min", min);
        Preconditions.checkValidU32("max", max);
        this.min = min;
        this.max = max;
    }

    public Limits(long min) {
        Preconditions.checkValidU32("min", min);
        this.min = min;
        this.max = null;
    }

    public long min() {
        return min;
    }

    public Long max() {
        return max;
    }
}
