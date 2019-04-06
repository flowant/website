/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowant.authserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flowant.website.AuthserverApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=AuthserverApplication.class)
@Log4j2
public class OAuth2ClientApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void index() throws Exception {
        ResultActions result = mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        log.trace(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void loginShouldHaveAllOAuth2ClientsToChooseFrom() throws Exception {
        ResultActions result = mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        String content = result.andReturn().getResponse().getContentAsString();
        log.trace(content);
        assertThat(content).contains("/facebook");
        assertThat(content).contains("/google");
    }
}
