package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.repository.BackendContentRepository;
import org.flowant.website.storage.FileStorage;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class ContentRestTest extends BaseRestWithRepositoryTest<Content, UUID, BackendContentRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ContentRest.CONTENT, Content.class, Content::getId,
                ContentMaker::smallRandom, ContentMaker::largeRandom,
                (Content c) -> {
                    c.setTitle("newTitle");
                    return c;}
                );
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

    @Test
    public void getPagination() {
        UUID containerId = UUIDs.timeBased();
        Flux<Content> contents = Flux.range(1, 10).map(i -> ContentMaker.smallRandom()
                .setContainerId(containerId)).cache();
        repo.saveAll(contents).blockLast();
        registerToBeDeleted(contents);

        webTestClient.get().uri(uriBuilder -> uriBuilder.path(ContentRest.CONTENT)
                .queryParam("cid", containerId.toString()).queryParam("page", "0")
                .queryParam("size", "3").build())
                .exchange()
                .expectStatus().isOk().expectBodyList(Content.class).consumeWith(r -> {
                    log.trace(r.getResponseHeaders()::toString);
                    r.getResponseBody().forEach(log::trace);
                });
        //TODO check link header and get all data
    }
}
