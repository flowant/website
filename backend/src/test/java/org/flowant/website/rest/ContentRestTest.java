package org.flowant.website.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.assertj.core.util.Lists;
import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.Tag;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

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
public class ContentRestTest extends BaseRestTest {
    @Autowired
    private ContentRepository contentRepository;

    Consumer<? super Content> deleteContent = u -> contentRepository.delete(u).subscribe();
    Consumer<? super Collection<Content>> deleteContents = l -> l.forEach(deleteContent);

    @Test
    public void testInsertMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notContent")), Tag.class).exchange();
        respSpec.expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters
    public void testInsert(Content content) {
        webTestClient.post().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
                    StepVerifier.create(contentRepository.findById(content.getId()))
                            .consumeNextWith(deleteContent).verifyComplete();
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
        contentRepository.save(content).block();
        webTestClient.get().uri(ContentRest.CONTENT__ID__, content.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Content.class).consumeWith( r -> {
                    log.trace(r);
                    ContentMaker.assertEqual(content, r.getResponseBody());
                    deleteContent.accept(content);
                });
    }
    public static List<Content> parametersForTestGetId() {
        return Arrays.asList(ContentMaker.smallRandom(), ContentMaker.largeRandom());
    }

    // @Test // used at an early development stage.
    public void testGetAllEmpty() {
        webTestClient.get().uri(ContentRest.CONTENT).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
    }

    // @Test // used at an early development stage.
    public void testGetAll() {
        Flux<Content> contents = Flux.range(1, 5).map(i -> ContentMaker.largeRandom()).cache();
        contentRepository.saveAll(contents).blockLast();

        webTestClient.get().uri(ContentRest.CONTENT).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Content.class).hasSize(5).consumeWith(r -> {
                    log.trace(r);
                    contents.subscribe(deleteContent);
                });
    }

    @Test
    public void testPut() {
        Content content = ContentMaker.largeRandom();
        contentRepository.save(content).block();

        content.setTitle("newTitle");

        webTestClient.put().uri(ContentRest.CONTENT).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
                .expectStatus().isOk().expectBody(Content.class).consumeWith( r -> {
                    log.trace(r::toString);
                    StepVerifier.create(contentRepository.findById(content.getId()))
                            .consumeNextWith(c -> ContentMaker.assertEqual(content, c))
                            .then(()-> deleteContent.accept(content)).verifyComplete();
                });
    }

    @Test
    public void testDelete() {
        Content content = ContentMaker.smallRandom();
        contentRepository.save(content).block();

        webTestClient.delete().uri(ContentRest.CONTENT__ID__, content.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(contentRepository.findById(content.getId())).expectNextCount(0).verifyComplete();
                });
    }

    @Test
    public void testDeleteWithFiles() {
        Content content = ContentMaker.smallRandom();
        FileRestTest.postFiles(3, webTestClient).consumeWith(body -> content.setFileRefs(body.getResponseBody()));

        contentRepository.save(content).block();

        webTestClient.delete().uri(ContentRest.CONTENT__ID__, content.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(contentRepository.findById(content.getId())).expectNextCount(0).verifyComplete();
                });
    }
}
