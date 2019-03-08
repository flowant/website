package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.util.test.UserMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class UserRestTest extends RestWithRepositoryTest<User, UUID, UserRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(UserRest.USER, User.class, User::getIdentity,
                UserMaker::smallRandom, UserMaker::largeRandom,
                user -> user.setFirstname("newFirstname"));

        setDeleter(user -> repo.deleteByIdWithRelationship(user.getIdentity()).subscribe());
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
