package org.flowant.authserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import org.flowant.website.AuthserverApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.devutil.MockUserRepoUtil;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.FormLoginRequestBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=AuthserverApplication.class)
@Log4j2
public class FormBasedAuthenticationTest {

    @Autowired
    MockUserRepoUtil mockUserRepoUtil;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithValidUserThenAuthenticated() throws Exception {
        User user = mockUserRepoUtil.saveUserWithEncodedPassword(UserMaker.large());

        FormLoginRequestBuilder login = formLogin().user(user.getUsername()).password(user.getPassword());
        mockMvc.perform(login)
            .andExpect(authenticated().withUsername(user.getUsername()));

        mockUserRepoUtil.deleteUser(user);
    }

    @Test
    public void loginWithRememberMe() throws Exception {
        User user = mockUserRepoUtil.saveUserWithEncodedPassword(UserMaker.large());

        MvcResult result = mockMvc.perform(post("/login").param("username", user.getUsername())
            .param("password", user.getPassword()).param("remember-me", "on").with(csrf()))
            .andExpect(authenticated().withUsername(user.getUsername())).andReturn();

        String rememberMe = result.getResponse().getCookie("remember-me").getValue();
        log.trace(rememberMe);

        mockMvc.perform(get("/user").cookie(new Cookie("remember-me", rememberMe)))
            .andExpect(authenticated().withUsername(user.getUsername()));

        mockUserRepoUtil.deleteUser(user);
    }

    @Test
    public void loginWithInvalidUserThenUnauthenticated() throws Exception {
        FormLoginRequestBuilder login = formLogin()
            .user("invalid")
            .password("invalidpassword");

        mockMvc.perform(login)
            .andExpect(unauthenticated());
    }

    @Test
    public void accessUnsecuredResourceThenOk() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk());
    }

    @Test
    public void accessSecuredResourceUnauthenticatedThenRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/user"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }
}
