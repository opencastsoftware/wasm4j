/*
 * SPDX-FileCopyrightText:  Copyright 2023 Opencast Software Europe Ltd
 * SPDX-License-Identifier: Apache-2.0
 */
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

public class WasmDisComparisonTest {
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

    private void assertWasmDisEquals(String fileName, String expected) throws IOException, InterruptedException {
        var wasmdis = new ProcessBuilder("wasm-dis", fileName)
                .directory(tmpDir.toFile())
                .start();

        wasmdis.waitFor();

        var watOutput = new BufferedReader(new InputStreamReader(wasmdis.getInputStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        var errorOutput = new BufferedReader(new InputStreamReader(wasmdis.getErrorStream()))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        System.err.println(errorOutput);

        assertThat(errorOutput, is(emptyString()));
        assertThat(wasmdis.exitValue(), is(0));

        assertThat(watOutput, is(equalTo(expected)));
    }

    @Test
    void emptyModule() throws Exception {
        var module = Module.empty();
        var fileName = "empty.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            assertWasmDisEquals(fileName,
                    String.join(
                            System.lineSeparator(),
                            "(module",
                            ")",
                            ""));
        });
    }

    @Test
    void moduleWithType() throws Exception {
        var intToIntType = ExternType.func(List.of(NumType.i32()), List.of(NumType.i32()));
        var module = Module.builder().withType(intToIntType).build();
        var fileName = "singletype.wasm";
        withBinaryFile(fileName, module, wasmFile -> {
            // It seems that wasm-dis strips out unused types
            assertWasmDisEquals(fileName, String.join(System.lineSeparator(),
                    "(module",
                    ")",
                    ""));
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
            assertWasmDisEquals(fileName,
                    String.join(System.lineSeparator(),
                            "(module",
                            " (type $i32_i32_=>_i32 (func (param i32 i32) (result i32)))",
                            " (func $0 (param $0 i32) (param $1 i32) (result i32)",
                            "  (i32.add",
                            "   (local.get $0)",
                            "   (local.get $1)",
                            "  )",
                            " )",
                            ")",
                            ""
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
            assertWasmDisEquals(fileName,
                    String.join(System.lineSeparator(),
                            "(module",
                            " (type $none_=>_none (func))",
                            " (start $0)",
                            " (func $0",
                            "  (nop)",
                            " )",
                            ")",
                            ""
                    )
            );
        });
    }

    @Disabled("Neither wasm-dis nor wasm2wat support the new table definition encoding with init expression")
    @Test
    void moduleWithTable() throws Exception {
        var module = Module.builder()
                .withTables(new Table(
                        ExternType.table(Limits.of(1), RefType.nullable(HeapType.func())),
                        ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func()))))
                .build();

        var fileName = "table.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasmDisEquals(fileName,
                    String.join(
                            System.lineSeparator(),
                            "(module",
                            " (memory $0 1)",
                            ")",
                            ""));
        });
    }


    @Test
    void moduleWithUnboundedMemory() throws Exception {
        var module = Module.builder()
                .withMemories(ExternType.mem(Limits.of(1)))
                .build();

        var fileName = "unboundedmem.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasmDisEquals(fileName,
                    String.join(
                            System.lineSeparator(),
                            "(module",
                            " (memory $0 1)",
                            ")",
                            ""));
        });
    }

    @Test
    void moduleWithBoundedMemory() throws Exception {
        var module = Module.builder()
                .withMemories(ExternType.mem(Limits.of(1, 5)))
                .build();

        var fileName = "boundedmem.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasmDisEquals(fileName,
                    String.join(
                            System.lineSeparator(),
                            "(module",
                            " (memory $0 1 5)",
                            ")",
                            ""));
        });
    }

    @Test
    void moduleWithGlobal() throws Exception {
        var module = Module.builder()
                .withGlobals(
                        new Global(
                                ExternType.global(false, NumType.i32()),
                                ConstantExpression.of(NumericInstruction.i32_const(1))),
                        new Global(
                                ExternType.global(true, RefType.nullable(HeapType.func())),
                                ConstantExpression.of(ReferenceInstruction.ref_null(HeapType.func())))
                )
                .build();

        var fileName = "globals.wasm";

        withBinaryFile(fileName, module, wasmFile -> {
            assertWasmDisEquals(fileName,
                    String.join(System.lineSeparator(),
                            "(module",
                            " (global $global$0 i32 (i32.const 1))",
                            " (global $global$1 (mut funcref) (ref.null nofunc))",
                            ")",
                            ""
                    ));
        });
    }
}
