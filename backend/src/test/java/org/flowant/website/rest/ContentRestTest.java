package org.flowant.website.rest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.Tag;
import org.flowant.website.repository.BackendContentRepository;
import org.flowant.website.storage.FileStorage;
import org.flowant.website.util.test.AssertUtil;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class ContentRestTest extends BaseRestWithRepositoryTest<Content, UUID, BackendContentRepository> {

    @Test
    public void testInsertMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notContent")), Tag.class).exchange();
        respSpec.expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testInsert(Content content) {
        registerToBeDeleted(content);

        webTestClient.post().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
                .expectStatus().isOk().expectBody(Content.class).consumeWith(r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(content, r.getResponseBody());
                    AssertUtil.assertEquals(content, repo.findById(content.getId()).block());
                });
    }
    public static List<Content> parametersForTestInsert() {
        return Arrays.asList(ContentMaker.smallRandom(), ContentMaker.largeRandom());
    }

    @Test
    public void testGetNotExist() {
        webTestClient.get().uri(ContentRest.CONTENT__ID__, UUID.randomUUID()).exchange()
                .expectStatus().isNotFound().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testGetMalformedId() {
        webTestClient.get().uri(ContentRest.CONTENT__ID__, "notExist").exchange()
                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testGetId(Content content) {
        repo.save(content).block();
        registerToBeDeleted(content);

        webTestClient.get().uri(ContentRest.CONTENT__ID__, content.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Content.class).consumeWith( r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(content, r.getResponseBody());
                });
    }
    public static List<Content> parametersForTestGetId() {
        return Arrays.asList(ContentMaker.smallRandom(), ContentMaker.largeRandom());
    }

    @Test
    public void testPut() {
        Content content = ContentMaker.largeRandom();
        repo.save(content).block();
        registerToBeDeleted(content);

        content.setTitle("newTitle");

        webTestClient.put().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
                .expectStatus().isOk().expectBody(Content.class).consumeWith( r -> {
                    log.trace(r::toString);
                    AssertUtil.assertEquals(content, r.getResponseBody());
                    AssertUtil.assertEquals(content, repo.findById(content.getId()));
                });
    }

    @Test
    public void testDelete() {
        Content content = ContentMaker.smallRandom();
        repo.save(content).block();
        registerToBeDeleted(content); // in case of fails

        webTestClient.delete().uri(ContentRest.CONTENT__ID__, content.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(repo.findById(content.getId())).expectNextCount(0).verifyComplete();
                });
    }

    @Test
    public void testDeleteWithFiles() {
        Content content = ContentMaker.smallRandom();
        FileRestTest.postFiles(3, webTestClient).consumeWith(body -> content.setFileRefs(body.getResponseBody()));

        repo.save(content).block();
        registerToBeDeleted(content); // in case of fails

        webTestClient.delete().uri(ContentRest.CONTENT__ID__, content.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    content.getFileRefs().forEach(fileRef -> Assert.assertFalse(FileStorage.exist(fileRef.getId())));
                    StepVerifier.create(repo.findById(content.getId())).expectNextCount(0).verifyComplete();
                });
    }
}
