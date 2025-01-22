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

import nl.altindag.senzu.exception.SenzuException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

class IOUtilsShould {

    @Test
    void getContentThrowsSenzuExceptionWhenStreamFailsToClose() throws IOException {
        ByteArrayInputStream inputStream = Mockito.spy(new ByteArrayInputStream("Hello".getBytes()));
        doThrow(new IOException("Could not read the content")).when(inputStream).close();

        assertThatThrownBy(() -> IOUtils.getContent(inputStream))
                .isInstanceOf(SenzuException.class)
                .hasRootCauseMessage("Could not read the content");
    }

}
