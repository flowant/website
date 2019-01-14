package org.flowant.backend.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.assertj.core.util.Lists;
import org.flowant.backend.model.Content;
import org.flowant.backend.model.ContentTest;
import org.flowant.backend.model.Tag;
import org.flowant.backend.repository.ContentRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        return Arrays.asList(ContentTest.small(), ContentTest.large());
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
                    ContentTest.assertEqual(content, r.getResponseBody());
                    deleteContent.accept(content);
                });
    }
    public static List<Content> parametersForTestGetId() {
        return Arrays.asList(ContentTest.small(), ContentTest.large());
    }

    @Test
    public void testGetAllEmpty() {
        webTestClient.get().uri(ContentRest.CONTENT).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
    }

    @Test
    public void testGetAll() {
        Flux<Content> contents = Flux.range(1, 5).map(ContentTest::large).cache();
        contentRepository.saveAll(contents).blockLast();

        webTestClient.get().uri(ContentRest.CONTENT).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Content.class).hasSize(5).consumeWith(r -> {
                    log.trace(r);
                    contents.subscribe(deleteContent);
                });
    }

    @Test
    public void testPutNotExist() {
        Content content = ContentTest.large();
        webTestClient.put().uri(ContentRest.CONTENT__ID__, content).contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
        .expectStatus().isOk().expectBody().consumeWith(r -> {
            log.trace(r);
            StepVerifier.create(contentRepository.findById(content.getId()))
                    .consumeNextWith(deleteContent).verifyComplete();
        });
    }

    @Test
    public void testPutMalformedId() {
        webTestClient.put().uri(ContentRest.CONTENT__ID__, "notExist").exchange()
                .expectStatus().isBadRequest().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testPut() {
        Content content = ContentTest.large();
        contentRepository.save(content).block();

        content.setTitle("newTitle");

        webTestClient.put().uri(ContentRest.CONTENT__ID__, content).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(content), Content.class).exchange()
                .expectStatus().isOk().expectBody(Content.class).consumeWith( r -> {
                    log.trace(r::toString);
                    StepVerifier.create(contentRepository.findById(content.getId()))
                            .consumeNextWith(c -> ContentTest.assertEqual(content, c))
                            .then(()-> deleteContent.accept(content)).verifyComplete();
                });
    }

    @Test
    public void testDelete() {
        Content content = ContentTest.small();
        contentRepository.save(content).block();

        webTestClient.delete().uri(ContentRest.CONTENT__ID__, content.getId()).exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(contentRepository.findById(content.getId())).expectNextCount(0).verifyComplete();
                });
    }
}
