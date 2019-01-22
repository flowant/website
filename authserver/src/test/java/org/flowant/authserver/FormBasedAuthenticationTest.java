package org.flowant.authserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=AuthserverApplication.class)
public class FormBasedAuthenticationTest {

    @Autowired
    MockUserRepoUtil mockUserRepoUtil;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithValidUserThenAuthenticated() throws Exception {
        User user = mockUserRepoUtil.saveUserWithEncodedPassword(UserMaker.small());
        FormLoginRequestBuilder login = formLogin().user(user.getEmail()).password(user.getPassword());
        mockMvc.perform(login)
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
        mockMvc.perform(get("/userInfo"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void accessSecuredResourceAuthenticatedThenOk() throws Exception {
        mockMvc.perform(get("/userInfo"))
                .andExpect(status().isOk());
    }
    
}
