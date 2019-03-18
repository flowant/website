package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.User;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.storage.FileStorage;
import org.flowant.website.util.test.UserMaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class UserRestTest extends RestWithRepositoryTest<User, UUID, UserRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(UserRest.PATH_USER, User.class, User::getIdentity,
                UserMaker::smallRandom, UserMaker::largeRandom,
                user -> user.setFirstname("newFirstname"));

        setDeleter(user -> repo.deleteByIdWithRelationship(user.getIdentity()).subscribe());
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testDeleteWithFiles() {
        User user = UserMaker.smallRandom();
        FileRestTest.postFiles(3, webTestClient).consumeWith(body -> user.setFileRefs(body.getResponseBody()));

        repo.save(user).block();
        cleaner.registerToBeDeleted(user); // in case of fails

        webTestClient.delete()
                .uri(UserRest.PATH_USER + UserRest.PATH_SEG_ID, user.getIdentity())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(r -> {
                    user.getFileRefs().forEach(fileRef -> Assert.assertFalse(FileStorage.exist(fileRef.getIdentity())));
                    StepVerifier.create(repo.findById(user.getIdentity())).expectNextCount(0).verifyComplete();
                });
    }

}
