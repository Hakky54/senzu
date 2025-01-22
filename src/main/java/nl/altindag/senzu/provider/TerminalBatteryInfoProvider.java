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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface TerminalBatteryInfoProvider extends BatteryInfoProvider {

    @Override
    default Optional<String> getBatteryLevel() {
        String[] command = getCommand();
        try(InputStream inputStream = createProcess(command).start().getInputStream()) {
            String content = IOUtils.getContent(inputStream);

            return Stream.of(content.split(System.lineSeparator()))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(getFilter())
                    .map(getMapper())
                    .findFirst();
        } catch (Exception e) {
            throw new SenzuException(e);
        }
    }

    /**
     * Wrapped for unit testing
     */
    static ProcessBuilder createProcess(String[] command) {
        return new ProcessBuilder(command);
    }

    String[] getCommand();

    Predicate<String> getFilter();

    Function<String, String> getMapper();

}
