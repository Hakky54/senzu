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
package nl.altindag.senzu.command;

import nl.altindag.senzu.util.OperatingSystem;
import picocli.CommandLine.Command;

@Command(
        name = "senzu",
        description = "CLI tool to get the battery percentage",
        mixinStandardHelpOptions = true
)
public class BatteryInfoCommand implements Runnable {

    @Override
    public void run() {
        String batteryLevel = OperatingSystem.get()
                .getBatteryInfoProvider()
                .getBatteryLevel()
                .orElse("Could not find battery information");

        System.out.println(batteryLevel);
    }

}
