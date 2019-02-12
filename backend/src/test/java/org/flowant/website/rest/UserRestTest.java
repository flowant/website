package org.flowant.website.rest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Tag;
import org.flowant.website.model.User;
import org.flowant.website.repository.BackendUserRepository;
import org.flowant.website.util.test.AssertUtil;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class UserRestTest extends BaseRestWithRepositoryTest<User, UUID, BackendUserRepository> {

    @Test
    public void testInsertMalformed() {
        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notUser")), Tag.class).exchange()
                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testInsert(User user) {
        registerToBeDeleted(user);

        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody(User.class).consumeWith(r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(user, r.getResponseBody());
                    AssertUtil.assertEquals(user, repo.findById(user.getId()).block());
                });
    }

    public static List<User> parametersForTestInsert() {
        return Arrays.asList(UserMaker.smallRandom(), UserMaker.largeRandom());
    }

    @Test
    public void testGetNotExist() {
        webTestClient.get().uri(UserRest.USER__ID__, UUID.randomUUID()).exchange()
                .expectStatus().isNotFound().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testGetMalformedId() {
        webTestClient.get().uri(UserRest.USER__ID__, "notExist").exchange()
                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testGetId(User user) {
        repo.save(user).block();
        registerToBeDeleted(user);

        webTestClient.get().uri(UserRest.USER__ID__, user.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(User.class).consumeWith( r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(user, r.getResponseBody());
                });
    }

    public static List<User> parametersForTestGetId() {
        return Arrays.asList(UserMaker.smallRandom(), UserMaker.largeRandom());
    }

    @Test
    public void testPut() {
        User user = UserMaker.largeRandom();
        repo.save(user).block();
        registerToBeDeleted(user);

        user.setFirstname("newFirstname");
        user.setFollowers(List.of(UUID.randomUUID(), UUID.randomUUID()));

        webTestClient.put().uri(UserRest.USER)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody(User.class).consumeWith( r -> {
                    log.trace(r::toString);
                    AssertUtil.assertEquals(user, r.getResponseBody());
                    AssertUtil.assertEquals(user, repo.findById(user.getId()).block());
                });
    }

    @Test
    public void testDelete() {
        User user = UserMaker.largeRandom();
        repo.save(user).block();
        registerToBeDeleted(user); // in case of fails

        webTestClient.delete().uri(UserRest.USER__ID__, user.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(repo.findById(user.getId())).expectNextCount(0).verifyComplete();
                });
    }

}
