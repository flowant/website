package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.User;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class UserRepositoryTest extends BaseRepositoryTest<User, UUID, UserRepository> {

    @Test
    public void crud() {
        testCrud(User::getId, UserMaker::smallRandom, UserMaker::largeRandom);
    }

    @Test
    public void findByUsername() {
        User user = UserMaker.largeRandom();
        deleteAfterTest(user);

        Flux<User> saveThenFind = repo.save(user).thenMany(repo.findByUsername(user.getUsername()));
        StepVerifier.create(saveThenFind).expectNextMatches(u -> user.getUsername().equals(u.getUsername()))
                .verifyComplete();
    }

}
