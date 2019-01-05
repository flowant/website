package org.flowant.backend.rest;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

import org.flowant.backend.model.Multimedia;
import org.flowant.backend.model.Tag;
import org.flowant.backend.repository.MultimediaRepository;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class MultimediaRestTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MultimediaRepository multimediaRepository;

    Consumer<? super Multimedia> deleteMultimedia = u -> multimediaRepository.delete(u).subscribe();
    Consumer<? super Collection<Multimedia>> deleteMultimedias = l -> l.forEach(deleteMultimedia);

//    @Test
    public void testPostMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(MultimediaRest.MULTIMEDIA).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notMultimedia")), Tag.class).exchange();
        respSpec.expectStatus().is4xxClientError().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testPost() {
        ClassPathResource img = new ClassPathResource("sea1.jpg");

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add(MultimediaRest.CID, UUID.randomUUID().toString());

        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", entity);

        webTestClient.post()
//                .uri(builder -> builder.path(MultimediaRest.MULTIMEDIA)
//                        .queryParam(MultimediaRest.CID, UUID.randomUUID()).build())
                .uri(MultimediaRest.MULTIMEDIA)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange().expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
//                    StepVerifier.create(multimediaRepository.findById(multimedia.getId()))
//                            .consumeNextWith(deleteMultimedia).verifyComplete();
                });
    }

    @Test
    public void testPosts() {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        for (int i = 2; i < 4; i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(MultimediaRest.CID, UUID.randomUUID().toString());

            HttpEntity<ClassPathResource> entity = new HttpEntity<>(
                    new ClassPathResource("sea" + i + ".jpg"), headers);

            parts.add("file", entity);
        }

        webTestClient.post()
//                .uri(builder -> builder.path(MultimediaRest.MULTIMEDIA)
//                        .queryParam(MultimediaRest.CID, UUID.randomUUID()).build())
                .uri(MultimediaRest.MULTIMEDIA + "s")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange().expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
//                    StepVerifier.create(multimediaRepository.findById(multimedia.getId()))
//                            .consumeNextWith(deleteMultimedia).verifyComplete();
                });
    }


//    @Test
//    public void testGetNotExist() {
//        webTestClient.get().uri(MultimediaRest.MULTIMEDIA__ID__, UUID.randomUUID()).exchange()
//                .expectStatus().isNotFound().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    public void testGetMalformedId() {
//        webTestClient.get().uri(MultimediaRest.MULTIMEDIA__ID__, "notExist").exchange()
//                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    @Parameters
//    public void testGetId(Multimedia multimedia) {
//        multimediaRepository.save(multimedia).block();
//        webTestClient.get().uri(MultimediaRest.MULTIMEDIA__ID__, multimedia.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(Multimedia.class).isEqualTo(multimedia).consumeWith( r -> {
//                    log.trace(r);
//                    deleteMultimedia.accept(multimedia);
//                });
//    }
//    public static List<Multimedia> parametersForTestGetId() {
//        return Arrays.asList(MultimediaTest.large());
//    }
//
//    @Test
//    public void testGetAllEmpty() {
//        multimediaRepository.deleteAll().block();
//        webTestClient.get().uri(MultimediaRest.MULTIMEDIA).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
//    }
//
//    @Test
//    public void testGetAll() {
//        Flux<Multimedia> multimedias = Flux.range(1, 5).map(MultimediaTest::large).cache();
//        multimediaRepository.deleteAll().thenMany(multimediaRepository.saveAll(multimedias)).blockLast();
//
//        webTestClient.get().uri(MultimediaRest.MULTIMEDIA).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBodyList(Multimedia.class).hasSize(5).consumeWith(r -> {
//                    log.trace(r);
//                    multimedias.subscribe(deleteMultimedia);
//                });
//    }
//
//    @Test
//    public void testPutNotExist() {
//        Multimedia multimedia = MultimediaTest.large();
//        webTestClient.put().uri(MultimediaRest.MULTIMEDIA__ID__, multimedia).contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(multimedia), Multimedia.class).exchange()
//        .expectStatus().isOk().expectBody().consumeWith(r -> {
//            log.trace(r);
//            StepVerifier.create(multimediaRepository.findById(multimedia.getId()))
//                    .consumeNextWith(deleteMultimedia).verifyComplete();
//        });
//    }
//
//    @Test
//    public void testPutMalformedId() {
//        webTestClient.put().uri(MultimediaRest.MULTIMEDIA__ID__, "notExist").exchange()
//                .expectStatus().isBadRequest().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    public void testPut() {
//        Multimedia multimedia = MultimediaTest.large();
//        multimediaRepository.save(multimedia).block();
//
//        webTestClient.put().uri(MultimediaRest.MULTIMEDIA__ID__, multimedia).contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(multimedia), Multimedia.class).exchange()
//                .expectStatus().isOk().expectBody().consumeWith( r -> {
//                    log.trace(r::toString);
//                    StepVerifier.create(multimediaRepository.findById(multimedia.getId())).expectNext(multimedia)
//                            .then(()-> deleteMultimedia.accept(multimedia)).verifyComplete();
//                });
//    }
//
//    @Test
//    public void testDelete() {
//        Multimedia multimedia = MultimediaTest.large();
//        multimediaRepository.save(multimedia).block();
//
//        webTestClient.delete().uri(MultimediaRest.MULTIMEDIA__ID__, multimedia.getId()).exchange()
//                .expectStatus().isOk().expectBody().consumeWith(r -> {
//                    log.trace(r::toString);
//                    StepVerifier.create(multimediaRepository.findById(multimedia.getId())).expectNextCount(0).verifyComplete();
//                });
//    }

}
