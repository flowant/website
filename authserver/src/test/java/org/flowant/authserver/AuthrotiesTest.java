package org.flowant.authserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flowant.website.AuthserverApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.devutil.MockUserRepoUtil;
import org.flowant.website.util.test.UserMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=AuthserverApplication.class)
@Log4j2
public class AuthrotiesTest {

    @Autowired
    MockUserRepoUtil mockUserRepoUtil;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockUserRepoUtil.findAllDeleteAll();
    }

    @Test
    public void accessWithValidRole() throws Exception {
        User user = mockUserRepoUtil.saveUserWithEncodedPassword(UserMaker.large());

        MvcResult result = mockMvc.perform(get("/user").with(httpBasic(user.getUsername(),user.getPassword())))
            .andExpect(status().isOk()).andReturn();

        log.trace(result);

        mockUserRepoUtil.deleteUser(user);
    }

    @Test
    public void accessWithoutValidRole() throws Exception {
        User user = mockUserRepoUtil.saveUserWithEncodedPassword(UserMaker.large());

        MvcResult result = mockMvc.perform(get("/admin").with(httpBasic(user.getUsername(),user.getPassword())))
            .andExpect(status().isForbidden()).andReturn();

        log.trace(result);

        mockUserRepoUtil.deleteUser(user);
    }


}
