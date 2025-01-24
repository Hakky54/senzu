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

import nl.altindag.senzu.provider.linux.Axp20xBatteryInfo;
import nl.altindag.senzu.provider.linux.UPowerBatteryInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LinuxBatteryInfo implements BatteryInfoProvider{

    private final List<BatteryInfoProvider> batteryInfoProviders = Arrays.asList(
            new UPowerBatteryInfo(),
            new Axp20xBatteryInfo()
    );

    @Override
    public Optional<String> getBatteryLevel() {
        return batteryInfoProviders.stream()
                .map(BatteryInfoProvider::getBatteryLevel)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

}
