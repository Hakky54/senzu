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
import nl.altindag.senzu.provider.TerminalBatteryInfoProvider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import picocli.CommandLine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    @Test
    void provideBatteryLevelForWindows() throws IOException {
        assertBatteryLevel("Windows", "terminal-output/windows.txt", new String[]{"WMIC", "PATH", "Win32_Battery", "Get", "EstimatedChargeRemaining"});
    }

    @Test
    void provideBatteryLevelForLinuxWithUPower() throws IOException {
        assertBatteryLevel("Linux", "terminal-output/linux/upower.txt", new String[]{"bash", "-c", "upower -i /org/freedesktop/UPower/devices/battery_BAT0"});
    }

    @Test
    void provideBatteryLevelForLinuxWithAxp20x() throws IOException {
        assertBatteryLevel("Linux", "terminal-output/linux/axp20x.txt", new String[]{"bash", "-c", "cat /sys/class/power_supply/axp20x-battery/capacity"});
    }

    @Test
    void notProvideBatteryLevelForLinuxWithWhileNotHavingAxp20x() throws IOException {
        assertBatteryLevel("Linux", "terminal-output/linux/no-axp20x.txt", new String[]{"bash", "-c", "cat /sys/class/power_supply/axp20x-battery/capacity"},
                logs -> assertThat(logs).contains("Could not find battery information"));
    }

    @Test
    void notProvideBatteryLevelForUnknownOs() {
        System.setProperty("os.name", "Magic OS");

        try(ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
            BatteryInfoCommand batteryInfoCommand = new BatteryInfoCommand();
            CommandLine cmd = new CommandLine(batteryInfoCommand);
            cmd.execute();

            assertThat(consoleCaptor.getStandardOutput()).contains("Could not find battery information");
        } finally {
            resetOsName();
        }
    }

    @Test
    void notProvideBatteryLevelForAndroid() {
        System.setProperty("os.name", "Linux");

        Map<String, String> systemProperties = new HashMap<>();
        systemProperties.put("java.vendor", "the android project");
        systemProperties.put("java.vm.vendor", "the android project");
        systemProperties.put("java.runtime.name", "android runtime");

        systemProperties.entrySet().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            try(ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
                BatteryInfoCommand batteryInfoCommand = new BatteryInfoCommand();
                CommandLine cmd = new CommandLine(batteryInfoCommand);
                cmd.execute();

                assertThat(consoleCaptor.getStandardOutput()).contains("Could not find battery information");
            } finally {
                System.clearProperty(entry.getKey());
            }
        });

        resetOsName();
    }

    void assertBatteryLevel(String osName, String mockTerminalOutputFile, String[] mockedArguments) throws IOException {
        assertBatteryLevel(osName, mockTerminalOutputFile, mockedArguments, logs -> {
            assertThat(logs.get(0))
                    .doesNotContain("Could not find battery information")
                    .containsOnlyDigits()
                    .hasSizeBetween(1, 3);
        });
    }

    void assertBatteryLevel(String osName, String mockTerminalOutputFile, String[] mockedArguments, Consumer<List<String>> assertion) throws IOException {
        System.setProperty("os.name", osName);
        InputStream inputStream = getResourceAsStream(mockTerminalOutputFile);

        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        ProcessBuilder toBeIgnoredProcessBuilder = mock(ProcessBuilder.class);
        Process process = mock(Process.class);
        Process toBeIgnoredProcess = mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.getInputStream()).thenReturn(inputStream);
        when(toBeIgnoredProcessBuilder.start()).thenReturn(toBeIgnoredProcess);
        when(toBeIgnoredProcess.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<TerminalBatteryInfoProvider> mockedStatic = mockStatic(TerminalBatteryInfoProvider.class, invocationOnMock -> {
            Method method = invocationOnMock.getMethod();
            if (method.getName().equals("createProcess")
                    && Arrays.asList((Object[]) invocationOnMock.getArguments()[0]).containsAll(Arrays.asList(mockedArguments))) {
                return processBuilder;
            } else {
                return toBeIgnoredProcessBuilder;
            }
        });
             ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {

            BatteryInfoCommand batteryInfoCommand = new BatteryInfoCommand();
            CommandLine cmd = new CommandLine(batteryInfoCommand);
            cmd.execute();

            List<String> standardOutput = consoleCaptor.getStandardOutput();
            assertThat(standardOutput).hasSize(1);
            assertion.accept(standardOutput);
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
