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
import picocli.CommandLine;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppShould {

    @Test
    void provideBatteryLevel() {
        BatteryInfoCommand batteryInfoCommand = new BatteryInfoCommand();
        CommandLine cmd = new CommandLine(batteryInfoCommand);

        try (ConsoleCaptor consoleCaptor = new ConsoleCaptor()) {
            cmd.execute();

            List<String> standardOutput = consoleCaptor.getStandardOutput();
            assertThat(standardOutput).hasSize(1);

            assertThat(standardOutput.get(0))
                    .doesNotContain("Could not find battery information")
                    .containsOnlyDigits()
                    .hasSizeBetween(1, 3);
        }
    }

}
