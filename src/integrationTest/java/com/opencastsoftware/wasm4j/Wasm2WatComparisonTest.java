package com.opencastsoftware.wasm4j;

import com.opencastsoftware.wasm4j.encoding.binary.WasmBinaryEncoder;
import com.opencastsoftware.wasm4j.instructions.control.ControlInstruction;
import com.opencastsoftware.wasm4j.instructions.numeric.NumericInstruction;
import com.opencastsoftware.wasm4j.instructions.reference.ReferenceInstruction;
import com.opencastsoftware.wasm4j.instructions.variable.VariableInstruction;
import com.opencastsoftware.wasm4j.types.*;
import org.apache.commons.lang3.function.FailableConsumer;
import org.junit.jupiter.api.Disabled;
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

public class Wasm2WatComparisonTest {
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

        wasm2wat.waitFor();

        var watOutput = new BufferedReader(new InputStreamReader(wasm2wat.getInputStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        var errorOutput = new BufferedReader(new InputStreamReader(wasm2wat.getErrorStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        System.err.println(errorOutput);

        assertThat(errorOutput, is(emptyString()));
        assertThat(wasm2wat.exitValue(), is(0));

        assertThat(
                watOutput.replaceAll("\\s+", " "),
                is(equalTo(expected.replaceAll("\\s+", " "))));
    }

    @Test
    void emptyModule() throws Exception {
        var module = Module.empty();
        var fileName = "empty.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module)");
        });
    }

    @Test
    void moduleWithType() throws Exception {
        var intToIntType = ExternType.func(List.of(NumType.i32()), List.of(NumType.i32()));
        var module = Module.builder().withType(intToIntType).build();
        var fileName = "singletype.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module (type (;0;) (func (param i32) (result i32))))");
        });
    }

    @Test
    void moduleWithAddFunction() throws Exception {
        // (i32, i32) -> i32
        var intToIntType = ExternType.func(List.of(NumType.i32(), NumType.i32()), List.of(NumType.i32()));

        var module = Module.builder()
                .withFunc(intToIntType, Expression.of(
                        VariableInstruction.local_get(0),
                        VariableInstruction.local_get(1),
                        NumericInstruction.i32_add()
                )).build();


        var fileName = "addfunction.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile,
                    String.join(System.lineSeparator(),
                            "(module",
                            "(type (;0;) (func (param i32 i32) (result i32)))",
                            "(func (;0;) (type 0) (param i32 i32) (result i32) local.get 0 local.get 1 i32.add))"
                    )
            );
        });
    }

    @Test
    void moduleWithStartFunction() throws Exception {
        // () -> ()
        var nullaryType = ExternType.func(List.of(), List.of());

        var module = Module.builder()
                .withFunc(nullaryType, Expression.of(ControlInstruction.nop()))
                .withStart(0)
                .build();

        var fileName = "startfunction.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile,
                    String.join(System.lineSeparator(),
                            "(module",
                            "(type (;0;) (func))",
                            "(func (;0;) (type 0) nop)",
                            "(start 0))"
                    )
            );
        });
    }

    @Test
    void moduleWithUnboundedMemory() throws Exception {
        var module = Module.builder()
                .withMemories(ExternType.mem(Limits.of(1)))
                .build();

        var fileName = "unboundedmem.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module (memory (;0;) 1))");
        });
    }

    @Test
    void moduleWithBoundedMemory() throws Exception {
        var module = Module.builder()
                .withMemories(ExternType.mem(Limits.of(1, 5)))
                .build();

        var fileName = "boundedmem.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module (memory (;0;) 1 5))");
        });
    }

    @Test
    void moduleWithGlobal() throws Exception {
        var module = Module.builder()
                .withGlobals(
                        new Global(
                                ExternType.global(false, NumType.i32()),
                                ConstantExpression.of(NumericInstruction.i32_const(1)))
                )
                .build();

        var fileName = "globals.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasm2WatEquals(fileName, wasmFile, "(module (global (;0;) i32 (i32.const 1)))");
        });
    }
}
