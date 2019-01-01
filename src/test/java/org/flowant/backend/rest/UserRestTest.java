package org.flowant.backend.rest;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.flowant.backend.model.CRUDZonedTime;
import org.flowant.backend.model.User;
import org.flowant.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = User.of(UUID.randomUUID(), "username1", "password1", "email1", CRUDZonedTime.now());
    }

    @Test
    public void testInsertUser() {

        webTestClient.post().uri("/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange().expectStatus()
                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody().jsonPath("$.id")
                .isNotEmpty().jsonPath("$.username").isEqualTo("username1");
    }

    @Test
    public void testGetAllUser() {
        webTestClient.get().uri("/user").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(User.class);
    }

    @Test
    public void testGetSingleUser() {

        User userInserted = repository.save(user).block();

        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.username")
                .isEqualTo("username1");
    }

    @Test
    public void testCollection() {
        user.setFollowers(Stream.generate(UUID::randomUUID).limit(5).collect(Collectors.toList()));
        user.setFollowing(Stream.generate(UUID::randomUUID).limit(5).collect(Collectors.toList()));

        User userInserted = repository.save(user).block();

        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.username")
                .isEqualTo("username1");

        user.setUsername("newUsername");

        webTestClient.put().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(user), User.class).exchange().expectStatus().isOk().expectBody().jsonPath("$.id")
                .isEqualTo(userInserted.getId().toString()).jsonPath("$.username").isEqualTo("newUsername");

    }

    @Test
    public void testUpdateSingleUser() {

        User UserInserted = repository.save(user).block();

        user.setUsername("newUsername");

        webTestClient.put().uri("/user/{id}", Collections.singletonMap("id", UserInserted.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(user), User.class).exchange().expectStatus().isOk().expectBody().jsonPath("$.id")
                .isEqualTo(UserInserted.getId().toString()).jsonPath("$.username").isEqualTo("newUsername");
    }

    @Test
    public void testDeleteUser() {
        User userInserted = repository.save(user).block();

        webTestClient.delete().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
                .expectStatus().isNotFound();
    }

}
