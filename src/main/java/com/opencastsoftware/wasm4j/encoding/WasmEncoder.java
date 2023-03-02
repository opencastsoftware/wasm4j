package com.opencastsoftware.wasm4j.encoding;

import java.io.OutputStream;

import com.opencastsoftware.wasm4j.Module;

public interface WasmEncoder {
    void encode(Module module, OutputStream output);
}
