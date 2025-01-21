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
package nl.altindag.senzu.provider.linux;

import nl.altindag.senzu.provider.TerminalBatteryInfoProvider;

import java.util.function.Function;
import java.util.function.Predicate;

public class Axp20xBatteryInfo extends TerminalBatteryInfoProvider {

    private static final String BATTERY_INFORMATION_FILE = "/sys/class/power_supply/axp20x-battery/capacity";

    @Override
    protected String[] getCommand() {
        return new String[]{"bash", "-c", "cat", BATTERY_INFORMATION_FILE};
    }

    @Override
    protected Predicate<String> getFilter() {
        return line -> !line.contains("No such file or directory");
    }

    @Override
    protected Function<String, String> getMapper() {
        return Function.identity();
    }

}
