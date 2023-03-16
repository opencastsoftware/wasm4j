package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.encoding.binary.WasmBinaryEncoder;
import com.opencastsoftware.wasm4j.types.ExternType;
import com.opencastsoftware.wasm4j.types.NumType;
import org.apache.commons.lang3.function.FailableConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class WabtComparisonTest {
    @TempDir
    Path tmpDir;

    private void withBinaryFile(String fileName, Module module, FailableConsumer<Path, Exception> test) throws Exception {
        var encoder = new WasmBinaryEncoder();
        var wasmFile = tmpDir.resolve(fileName);
        Files.createFile(wasmFile);

        try (var output = new FileOutputStream(wasmFile.toFile())) {
            encoder.encodeModule(output, module);
            test.accept(wasmFile);
        }
    }

    private void assertWasm2WatEquals(String fileName, Path wasmFile, String expected) throws IOException, InterruptedException {
        var wasm2wat = new ProcessBuilder("wasm2wat", fileName)
                .directory(tmpDir.toFile())
                .start();

        var watOutput = new BufferedReader(new InputStreamReader(wasm2wat.getInputStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        var errorOutput = new BufferedReader(new InputStreamReader(wasm2wat.getErrorStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        wasm2wat.waitFor();

        assertThat(errorOutput, is(emptyString()));
        assertThat(wasm2wat.exitValue(), is(0));
        assertThat(watOutput.replaceAll("\\s+", " "), is(equalToCompressingWhiteSpace(expected)));
    }

    @Test
    void emptyModuleMatchesWabt() throws Exception {
        var module = Module.empty();
        var fileName = "empty.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module)");
        });
    }

    @Test
    void moduleWithTypeMatchesWabt() throws Exception {
        var module = Module.empty();
        var intToIntType = ExternType.func(List.of(NumType.i32()), List.of(NumType.i32()));
        module.types().add(intToIntType);
        var fileName = "singletype.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module (type (;0;) (func (param i32) (result i32))))");
        });
    }
}
