package org.flowant.backend.rest;

import java.util.Arrays;
import java.util.List;

import org.flowant.backend.model.User;
import org.flowant.backend.model.UserMaker;
import org.flowant.backend.repository.UserRepository;
import org.junit.Before;
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

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import reactor.core.publisher.Mono;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {}

    // find doesn't exist item and check error

    // make user
    // insert user and check and delete and check
    // insert users and check and delete all and check
    // modefy user and check and delete and check

    @Test
    @Parameters
    public void testInsertUser(User user) {
        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class)
                .exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(User.class).consumeWith(RestLogger::log).isEqualTo(user);
    }
    public static List<User> parametersForTestInsertUser() {
        return Arrays.asList(UserMaker.small(), UserMaker.large());
    }

//    @Test
//    public void testInsertUser() {
//        User user = UserMaker.large();
//        webTestClient.post().uri(UserRest.USER).contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class)
//                .exchange().expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(User.class).consumeWith(RestLogger::log).isEqualTo(user);
//    }

//    
//    @Test
//    public void aaatestInsertUser() {
//        webTestClient.post().uri("/user").contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(user), User.class).exchange().expectStatus()
//                .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBody().consumeWith(RestLogger::log).jsonPath("$.id")
//                .isNotEmpty().jsonPath("$.username").isEqualTo("username1");
//    }
//
//    @Test
//    public void testGetAllUser() {
//        webTestClient.get().uri("/user").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(User.class);
//    }
//
//    @Test
//    public void testGetSingleUser() {
//
//        User userInserted = userRepository.save(user).block();
//
//        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
//                .expectStatus().isOk().expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.username")
//                .isEqualTo("username1");
//    }
//
//    @Test
//    public void testCollection() {
//        user.setFollowers(Stream.generate(UUID::randomUUID).limit(5).collect(Collectors.toList()));
//        user.setFollowings(Stream.generate(UUID::randomUUID).limit(5).collect(Collectors.toList()));
//
//        User userInserted = userRepository.save(user).block();
//
//        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
//                .expectStatus().isOk().expectBody().jsonPath("$.id").isNotEmpty().jsonPath("$.username")
//                .isEqualTo("username1");
//
//        user.setUsername("newUsername");
//
//        webTestClient.put().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId()))
//                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(user), User.class).exchange().expectStatus().isOk().expectBody().jsonPath("$.id")
//                .isEqualTo(userInserted.getId().toString()).jsonPath("$.username").isEqualTo("newUsername");
//
//    }
//
//    @Test
//    public void testUpdateSingleUser() {
//
//        User UserInserted = userRepository.save(user).block();
//
//        user.setUsername("newUsername");
//
//        webTestClient.put().uri("/user/{id}", Collections.singletonMap("id", UserInserted.getId()))
//                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON_UTF8)
//                .body(Mono.just(user), User.class).exchange().expectStatus().isOk().expectBody().jsonPath("$.id")
//                .isEqualTo(UserInserted.getId().toString()).jsonPath("$.username").isEqualTo("newUsername");
//    }
//
//    @Test
//    public void testDeleteUser() {
//        User userInserted = userRepository.save(user).block();
//
//        webTestClient.delete().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
//                .expectStatus().isOk();
//
//        webTestClient.get().uri("/user/{id}", Collections.singletonMap("id", userInserted.getId())).exchange()
//                .expectStatus().isNotFound();
//    }

}
