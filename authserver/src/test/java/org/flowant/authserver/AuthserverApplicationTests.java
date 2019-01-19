package org.flowant.authserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Log4j2
public class AuthserverApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    //TODO clean up
    @Value("${dev.user}")
    String user;
    @Value("${dev.userPassword}")
    String userPassword;
    @Value("${dev.client}")
    String client;
    @Value("${dev.clientPassword}")
    String clientPassword;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testGetJWKs() throws Exception {
        ResultActions result = mockMvc.perform(get("/.well-known/jwks.json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));

        log.trace(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get("/oauth/token")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testClientCredentialsAccessToken() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        ResultActions result = mockMvc
                .perform(post("/oauth/token").params(params).with(httpBasic(client, clientPassword))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        log.trace(result.andReturn().getResponse().getContentAsString());
    }

    public String obtainPasswordAccessToken(String username, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result = mockMvc
                .perform(post("/oauth/token").params(params).with(httpBasic(client, clientPassword))
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        log.trace(resultString);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void testPasswordAccessToken() throws Exception {
        obtainPasswordAccessToken(user, userPassword);
    }

//    @Test
//    public void testPasswordAccessTokenForbidden() throws Exception {
//        String accessToken = obtainPasswordAccessToken(user, userPassword);
//        ResultActions result = mockMvc.perform(get("/admin").header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isForbidden());
//
//        log.trace(result.andReturn().getResponse().getContentAsString());
//    }

// TODO why?
//    @Test
//    public void testPasswordAccessTokenGranted() throws Exception {
//        String accessToken = obtainPasswordAccessToken(user, userPassword);
//        ResultActions result = mockMvc.perform(get("/user").header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"));
//
//        log.trace(result.andReturn().getResponse().getContentAsString());
//    }
}
