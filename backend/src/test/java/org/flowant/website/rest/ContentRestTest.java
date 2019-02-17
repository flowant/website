package org.flowant.website.rest;

import static org.flowant.website.rest.BaseRepositoryRest.CID;
import static org.flowant.website.rest.BaseRepositoryRest.PAGE;
import static org.flowant.website.rest.BaseRepositoryRest.SIZE;

import java.util.Optional;
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
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

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

        ClientResponse resp = WebClient.create().get().uri(uriBuilder ->
            uriBuilder.scheme(SCHEME).host(host).port(port)
                .path(ContentRest.CONTENT)
                .queryParam(CID, containerId.toString()).queryParam(PAGE, "0")
                .queryParam(SIZE, "3").build())
                .exchange().block();

        Optional<String> nextUrl = LinkUtil.getNextUrl(resp.headers().asHttpHeaders());
        log.trace("nextUrl:{}", nextUrl);

        resp.bodyToFlux(Content.class).subscribe(log::trace);
        //TODO check link header and get all data
    }

    @Test
    public void nextUrl() {
        String value = "<http://127.0.0.1:38399/content?cid=b5a468c0-3264-11e9-8573-9352a7858433&page=2&size=3&ps=0018001010b5a86062326411e985739352a785843300f07ffffffc009a1b1e4a88b456bfc3fc6bdb87bba5bd0004>; rel=\"next\"";
        Assert.assertEquals("http://127.0.0.1:38399/content?cid=b5a468c0-3264-11e9-8573-9352a7858433&page=2&size=3&ps=0018001010b5a86062326411e985739352a785843300f07ffffffc009a1b1e4a88b456bfc3fc6bdb87bba5bd0004",
                LinkUtil.parseNextUrl(value));
    }
}
