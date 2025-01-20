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

import nl.altindag.senzu.provider.BatteryInfoProvider;
import nl.altindag.senzu.provider.LinuxBatteryInfo;
import nl.altindag.senzu.provider.MacBatteryInfo;
import nl.altindag.senzu.provider.NoBatterInfoProvider;
import nl.altindag.senzu.provider.WindowsBatteryInfo;

/**
 * @author Hakan Altindag
 */
public enum OperatingSystem {

    MAC(new MacBatteryInfo()),
    LINUX(new LinuxBatteryInfo()),
    ANDROID(NoBatterInfoProvider.getInstance()),
    WINDOWS(new WindowsBatteryInfo()),
    UNKNOWN(NoBatterInfoProvider.getInstance());

    private final BatteryInfoProvider batteryInfoProvider;

    OperatingSystem(BatteryInfoProvider batteryInfoProvider) {
        this.batteryInfoProvider = batteryInfoProvider;
    }

    static String getResolvedOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static OperatingSystem get() {
        String operatingSystem = getResolvedOsName();
        if (operatingSystem.contains("windows")) {
            return WINDOWS;
        }

        if (operatingSystem.contains("mac")) {
            return MAC;
        }

        if (operatingSystem.contains("linux")) {
            String javaVendor = System.getProperty("java.vendor", "").toLowerCase();
            String javaVmVendor = System.getProperty("java.vm.vendor", "").toLowerCase();
            String javaRuntimeName = System.getProperty("java.runtime.name", "").toLowerCase();

            if (javaVendor.equals("the android project")
                    || javaVmVendor.equals("the android project")
                    || javaRuntimeName.equals("android runtime")) {

                return ANDROID;
            } else {
                return LINUX;
            }
        }

        return UNKNOWN;
    }

    public BatteryInfoProvider getBatteryInfoProvider() {
        return batteryInfoProvider;
    }

}
