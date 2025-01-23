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

public class UPowerBatteryInfo implements TerminalBatteryInfoProvider {

    private static final Predicate<String> CONTAINS_PERCENTAGE_KEY = line -> line.contains("percentage:");
    private static final Predicate<String> DOES_NOT_CONTAINS_ZERO_PERCENTAGE_VALUE = line -> !line.contains("0% (should be ignored)");

    @Override
    public String[] getCommand() {
        return new String[]{"bash", "-c", "upower -i /org/freedesktop/UPower/devices/battery_BAT0"};
    }

    @Override
    public Predicate<String> getFilter() {
        return CONTAINS_PERCENTAGE_KEY.and(DOES_NOT_CONTAINS_ZERO_PERCENTAGE_VALUE);
    }

    @Override
    public Function<String, String> getMapper() {
        return line -> line.split(":")[1]
                .trim()
                .replace("%", "");
    }

}
