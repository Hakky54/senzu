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

import java.util.function.Function;
import java.util.function.Predicate;

public class LinuxBatteryInfo extends TerminalBatteryInfoProvider {

    private static final String SYSTEM_POWER_INFORMATION_COMMAND = "upower -i /org/freedesktop/UPower/devices/battery_BAT0";

    @Override
    String[] getCommand() {
        return new String[]{"bash", "-c", SYSTEM_POWER_INFORMATION_COMMAND};
    }

    @Override
    Predicate<String> getFilter() {
        return line -> line.contains("percentage:");
    }

    @Override
    Function<String, String> getMapper() {
        return line -> line.split(":")[1].trim();
    }

}
