package org.flowant.backend.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.assertj.core.util.Lists;
import org.flowant.backend.model.Tag;
import org.flowant.backend.model.User;
import org.flowant.backend.model.UserMaker;
import org.flowant.backend.repository.UserRepository;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class UserRestTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    Consumer<? super User> deleteUser = u -> userRepository.delete(u).subscribe();
    Consumer<? super Collection<User>> deleteUsers = l -> l.forEach(deleteUser);

    @Test
    public void testInsertMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notUser")), Tag.class).exchange();
        respSpec.expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testInsert(User user) {
        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
                    StepVerifier.create(userRepository.findById(user.getId()))
                            .consumeNextWith(deleteUser).verifyComplete();
                });
    }

    public static List<User> parametersForTestInsert() {
        return Arrays.asList(UserMaker.small(), UserMaker.large());
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
                .expectBody(User.class).isEqualTo(user).consumeWith( r -> {
                    log.trace(r);
                    deleteUser.accept(user);
                });
    }

    public static List<User> parametersForTestGetId() {
        return Arrays.asList(UserMaker.small(), UserMaker.large());
    }

    @Test
    public void testGetAllEmpty() {
        userRepository.deleteAll().block();
        webTestClient.get().uri(UserRest.USER).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
    }

    @Test
    public void testGetAll() {
        Flux<User> users = Flux.range(1, 5).map(UserMaker::small).cache();
        userRepository.deleteAll().thenMany(userRepository.saveAll(users)).blockLast();

        webTestClient.get().uri(UserRest.USER).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(User.class).hasSize(5).consumeWith(r -> {
                    log.trace(r);
                    users.subscribe(deleteUser);
                });
    }

    @Test
    public void testPutNotExist() {
        User user = UserMaker.large();
        webTestClient.put().uri(UserRest.USER__ID__, user).contentType(MediaType.APPLICATION_JSON_UTF8)
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
        User user = UserMaker.large();
        userRepository.save(user).block();

        user.setFirstname("newFirstname");
        user.setFollowers(List.of(UUID.randomUUID(), UUID.randomUUID()));

        webTestClient.put().uri(UserRest.USER__ID__, user).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange()
                .expectStatus().isOk().expectBody().consumeWith( r -> {
                    log.trace(r::toString);
                    StepVerifier.create(userRepository.findById(user.getId())).expectNext(user)
                            .then(()-> deleteUser.accept(user)).verifyComplete();
                });
    }

    @Test
    public void testDelete() {
        User user = UserMaker.large();
        userRepository.save(user).block();

        webTestClient.delete().uri(UserRest.USER__ID__, user.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(userRepository.findById(user.getId())).expectNextCount(0).verifyComplete();
                });
    }

}
