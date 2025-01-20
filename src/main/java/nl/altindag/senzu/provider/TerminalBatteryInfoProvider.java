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
import nl.altindag.senzu.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class TerminalBatteryInfoProvider implements BatteryInfoProvider {

    @Override
    public String getBatteryLevel() {
        String command = getCommand();
        try(InputStream inputStream = createProcess(command).getInputStream()) {
            String content = IOUtils.getContent(inputStream);

            return Stream.of(content.split(System.lineSeparator()))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(getFilter())
                    .map(getMapper())
                    .findFirst()
                    .orElse("Could not find battery information");

        } catch (IOException e) {
            throw new SenzuException(e);
        }
    }

    private static Process createProcess(String command) {
        try {
            return Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new SenzuException(e);
        }
    }

    abstract String getCommand();

    abstract Predicate<String> getFilter();

    abstract Function<String, String> getMapper();

}
