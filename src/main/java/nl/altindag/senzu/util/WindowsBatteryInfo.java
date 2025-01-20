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
package nl.altindag.senzu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

@SuppressWarnings("SameParameterValue")
public class WindowsBatteryInfo implements BatteryInfoProvider {

    private static final String SYSTEM_POWER_INFORMATION_COMMAND = "WMIC PATH Win32_Battery Get EstimatedChargeRemaining";

    @Override
    public String getBatteryLevel() {
        try(InputStream inputStream = createProcess(SYSTEM_POWER_INFORMATION_COMMAND).getInputStream()) {
            String content = IOUtils.getContent(inputStream);

            return Stream.of(content.split(System.lineSeparator()))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.contains("EstimatedChargeRemaining"))
                    .findFirst()
                    .orElse("Could not find battery information");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Process createProcess(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
