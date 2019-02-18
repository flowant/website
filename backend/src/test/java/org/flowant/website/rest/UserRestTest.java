package org.flowant.website.rest;

import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.BackendUserRepository;
import org.flowant.website.util.test.UserMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class UserRestTest extends RestWithRepositoryTest<User, UUID, BackendUserRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(UserRest.USER, User.class, User::getIdentity,
                UserMaker::smallRandom, UserMaker::largeRandom,
                (User user) -> {
                    user.setFirstname("newFirstname");
                    user.setFollowers(List.of(UUIDs.timeBased(), UUIDs.timeBased()));
                    return user;
                });
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
