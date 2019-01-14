package org.flowant.backend.rest;

import org.flowant.backend.model.FileRef;
import org.flowant.backend.model.Tag;
import org.flowant.backend.storage.FileStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class FileRestTest extends BaseRestTest {
    @Test
    public void testPostMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(FileRest.FILES).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notFile")), Tag.class).exchange();
        respSpec.expectStatus().is4xxClientError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters({"1", "3"})
    public void testPosts(int count) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        for (int i = 1; i <= count; i++) {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ClassPathResource> entity = new HttpEntity<>(
                    new ClassPathResource("sea" + i + ".jpg"), headers);
            parts.add(FileRest.ATTACHMENT, entity);
        }

        webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(parts)).exchange()
        .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(count).consumeWith(body -> {
            body.getResponseBody().forEach(fileRef -> {
                log.trace("post files response:{}", fileRef);
                StepVerifier.create(FileStorage.findById(fileRef.getId()))
                        .expectNextCount(1).verifyComplete();
                FileStorage.deleteById(fileRef.getId()).subscribe();
            });
        });
    }

    @Test
    public void testGetNotExistId() {
        webTestClient.get().uri(FileRest.FILES__ID__, "NotExist").exchange()
        .expectStatus().isNotFound()
        .expectBody().consumeWith(log::trace);
    }

    @Test
    public void testGetId() {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        ClassPathResource img = new ClassPathResource("sea1.jpg");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
        parts.add(FileRest.ATTACHMENT, entity);

        webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(parts)).exchange()
        .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(1).consumeWith(body -> {
            body.getResponseBody().forEach(fileRef -> {
                webTestClient.get().uri(FileRest.FILES__ID__, fileRef.getId()).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith( s -> {
                    Assert.assertTrue(0 < s.getResponseBodyContent().length);
                    FileStorage.deleteById(fileRef.getId()).subscribe();
                });
            });
        });
    }

    @Test
    public void testDeleteNotExistId() {
        webTestClient.delete().uri(FileRest.FILES__ID__, "NotExist").exchange()
        .expectStatus().isNotFound()
        .expectBody().consumeWith(log::trace);
    }

    @Test
    public void testDeleteId() {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        ClassPathResource img = new ClassPathResource("sea1.jpg");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
        parts.add(FileRest.ATTACHMENT, entity);

        webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(parts)).exchange()
        .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(1).consumeWith(body -> {
            body.getResponseBody().forEach(fileRef -> {
                webTestClient.delete().uri(FileRest.FILES__ID__, fileRef.getId()).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(s -> {
                    log.trace(s);
                    FileStorage.findById(fileRef.getId()).subscribe(res -> Assert.assertFalse(res.exists()));
                });
            });
        });
    }
}
