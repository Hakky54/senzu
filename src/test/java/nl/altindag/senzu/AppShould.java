/*
 * Copyright 2025 Thunderberry.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.altindag.senzu;

import nl.altindag.console.ConsoleCaptor;
import nl.altindag.senzu.command.BatteryInfoCommand;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class AppShould {

    private static final String ORIGINAL_OS_NAME = System.getProperty("os.name");

    @Test
    void provideBatteryLevelForMac() throws IOException {
        assertBatteryLevel("Mac OS X", "terminal-output/mac.txt", new String[]{"system_profiler", "SPPowerDataType"});
    }

    void assertBatteryLevel(String osName, String mockTerminalOutputFile, String[] mockedArguments) throws IOException {
        InputStream mac = getResourceAsStream(mockTerminalOutputFile);

        try (MockedStatic<Runtime> runtimeMockedStatic = mockStatic(Runtime.class);
             ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {

            Runtime runtime = mock(Runtime.class);
            Process process = mock(Process.class);

            runtimeMockedStatic.when(Runtime::getRuntime).thenReturn(runtime);
            when(runtime.exec(mockedArguments)).thenReturn(process);
            when(process.getInputStream()).thenReturn(mac);

            BatteryInfoCommand batteryInfoCommand = new BatteryInfoCommand();
            CommandLine cmd = new CommandLine(batteryInfoCommand);

            System.setProperty("os.name", osName);

            cmd.execute();

            List<String> standardOutput = consoleCaptor.getStandardOutput();
            assertThat(standardOutput).hasSize(1);

            assertThat(standardOutput.get(0))
                    .doesNotContain("Could not find battery information")
                    .containsOnlyDigits()
                    .hasSizeBetween(1, 3);
        } finally {
            resetOsName();
        }
    }

    private static InputStream getResourceAsStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    private static void resetOsName() {
        System.setProperty("os.name", ORIGINAL_OS_NAME);
    }

}
