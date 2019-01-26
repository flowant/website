package org.flowant.website.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.assertj.core.util.Lists;
import org.flowant.website.BackendApplication;
import org.flowant.website.model.Tag;
import org.flowant.website.model.User;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.rest.UserRest;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class UserRestTest extends BaseRestTest {
    @Autowired
    private UserRepository userRepository;

    Consumer<? super User> deleteUser = u -> userRepository.delete(u).subscribe();
    Consumer<? super Collection<User>> deleteUsers = l -> l.forEach(deleteUser);

    @Test
    public void testInsertMalformed() {
        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notUser")), Tag.class).exchange()
                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testInsert(User user) {
        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody(User.class).consumeWith(r -> {
                    log.trace(r);
                    UserMaker.assertEqual(user, r.getResponseBody());
                    StepVerifier.create(userRepository.findById(user.getId()))
                            .consumeNextWith(deleteUser).verifyComplete();
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
        userRepository.save(user).block();
        webTestClient.get().uri(UserRest.USER__ID__, user.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(User.class).consumeWith( r -> {
                    log.trace(r);
                    UserMaker.assertEqual(user, r.getResponseBody());
                    deleteUser.accept(user);
                });
    }

    public static List<User> parametersForTestGetId() {
        return Arrays.asList(UserMaker.smallRandom(), UserMaker.largeRandom());
    }

    @Test
    public void testGetAllEmpty() {
        webTestClient.get().uri(UserRest.USER).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
    }

    @Test
    public void testGetAll() {
        Flux<User> users = Flux.range(1, 5).map(i ->UserMaker.largeRandom()).cache();
        userRepository.saveAll(users).blockLast();
        webTestClient.get().uri(UserRest.USER).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(User.class).hasSize(5).consumeWith(r -> {
                    log.trace(r);
                    users.subscribe(deleteUser);
                });
    }

    @Test
    public void testPutNotExist() {
        User user = UserMaker.largeRandom();
        webTestClient.put().uri(UserRest.USER__ID__, user.getId()).contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
        .expectStatus().isOk().expectBody().consumeWith(r -> {
            log.trace(r);
            StepVerifier.create(userRepository.findById(user.getId()))
                    .consumeNextWith(deleteUser).verifyComplete();
        });
    }

    @Test
    public void testPutMalformedId() {
        webTestClient.put().uri(UserRest.USER__ID__, "notExist").exchange()
                .expectStatus().isBadRequest().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testPut() {
        User user = UserMaker.largeRandom();
        userRepository.save(user).block();

        user.setFirstname("newFirstname");
        user.setFollowers(List.of(UUID.randomUUID(), UUID.randomUUID()));

        webTestClient.put().uri(UserRest.USER__ID__, user.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody().consumeWith( r -> {
                    log.trace(r::toString);
                    StepVerifier.create(userRepository.findById(user.getId()))
                            .consumeNextWith(actual -> UserMaker.assertEqual(user, actual))
                            .then(()-> deleteUser.accept(user)).verifyComplete();
                });
    }

    @Test
    public void testDelete() {
        User user = UserMaker.largeRandom();
        userRepository.save(user).block();
        webTestClient.delete().uri(UserRest.USER__ID__, user.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(userRepository.findById(user.getId())).expectNextCount(0).verifyComplete();
                });
    }
}
