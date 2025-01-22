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
package nl.altindag.senzu.provider;

import nl.altindag.senzu.exception.SenzuException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TerminalBatteryInfoProviderShould {

    private static final String ORIGINAL_OS_NAME = System.getProperty("os.name");

    @Test
    void shouldWrapExceptionIntoSenzuException() throws IOException {
        BatteryInfoProvider batteryInfoProvider = new TerminalBatteryInfoProvider() {

            @Override
            public String[] getCommand() {
                return new String[0];
            }

            @Override
            public Predicate<String> getFilter() {
                return line -> true;
            }

            @Override
            public Function<String, String> getMapper() {
                return Function.identity();
            }
        };

        System.setProperty("os.name", "Mac OS X");
        InputStream inputStream = spy(getResourceAsStream("terminal-output/mac.txt"));
        doThrow(new IOException("KABOOM!")).when(inputStream).close();

        Process process = mock(Process.class);
        when(process.getInputStream()).thenReturn(inputStream);

        try (MockedStatic<TerminalBatteryInfoProvider> mockedStatic = mockStatic(TerminalBatteryInfoProvider.class)) {

            mockedStatic.when(() -> TerminalBatteryInfoProvider.createProcess(any(String[].class))).thenReturn(process);

            assertThatThrownBy(batteryInfoProvider::getBatteryLevel)
                    .isInstanceOf(SenzuException.class)
                    .hasRootCauseMessage("KABOOM!");
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
