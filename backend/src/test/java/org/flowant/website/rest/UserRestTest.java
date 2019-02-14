package org.flowant.website.rest;

import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.BackendUserRepository;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class UserRestTest extends BaseRestWithRepositoryTest<User, UUID, BackendUserRepository> {

    @Test
    public void testCrud() {
        super.testCrud(UserRest.USER, User.class, User::getId,
                UserMaker::smallRandom, UserMaker::largeRandom,
                (User user) -> {
                    user.setFirstname("newFirstname");
                    user.setFollowers(List.of(UUID.randomUUID(), UUID.randomUUID()));
                    return user;}
                );
    }

}
