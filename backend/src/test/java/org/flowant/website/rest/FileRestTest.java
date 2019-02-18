package org.flowant.website.rest;

import java.util.List;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.FileRef;
import org.flowant.website.model.Tag;
import org.flowant.website.storage.FileStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

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
public class FileRestTest extends RestTest {
    @Test
    public void testPostMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(FileRest.FILES).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notFile")), Tag.class).exchange();
        respSpec.expectStatus().is4xxClientError().expectBody().consumeWith(log::trace);
    }

    public static ListBodySpec<FileRef> postFiles(int count, WebTestClient webTestClient) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        for (int i = 1; i <= count; i++) {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ClassPathResource> entity = new HttpEntity<>(
                    new ClassPathResource("sea" + i + ".jpg"), headers);
            parts.add(FileRest.ATTACHMENT, entity);
        }

        return webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts)).exchange()
                .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(count);
    }

    @Test
    @Parameters({"1", "3"})
    public void testPosts(int count) {
        postFiles(count, webTestClient).consumeWith(body -> {
            // log.trace(body); // use if Http requests need to be debugged.
            body.getResponseBody().forEach(fileRef -> {
                log.trace("post files response:{}", fileRef);
                StepVerifier.create(FileStorage.findById(fileRef.getIdentity()))
                        .expectNextCount(1).verifyComplete();
                FileStorage.deleteById(fileRef.getIdentity()).subscribe();
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
                webTestClient.get().uri(FileRest.FILES__ID__, fileRef.getIdentity()).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith( s -> {
                    Assert.assertTrue(0 < s.getResponseBodyContent().length);
                    FileStorage.deleteById(fileRef.getIdentity()).subscribe();
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
                webTestClient.delete().uri(FileRest.FILES__ID__, fileRef.getIdentity()).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(s -> {
                    log.trace(s);
                    FileStorage.findById(fileRef.getIdentity()).subscribe(res -> Assert.assertFalse(res.exists()));
                });
            });
        });
    }

    @Test
    public void testDeleteAll() {
        postFiles(3, webTestClient).consumeWith(body -> {
            List<FileRef> refs = body.getResponseBody();

            webTestClient.post().uri(FileRest.FILES_DELETES).contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Flux.fromIterable(refs), FileRef.class).exchange()
            .expectStatus().isOk().expectBody().consumeWith(log::trace);
        });
    }
}
