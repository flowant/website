package org.flowant.website.rest;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.storage.FileStorage;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class ContentRestTest extends RestWithRepositoryTest<Content, IdCid, ContentRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ContentRest.CONTENT, Content.class, Content::getIdCid,
                ContentMaker::smallRandom, ContentMaker::largeRandom,
                c -> c.setTitle("newTitle"));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testDeleteWithFiles() {
        Content content = ContentMaker.smallRandom();
        FileRestTest.postFiles(3, webTestClient).consumeWith(body -> content.setFileRefs(body.getResponseBody()));

        repo.save(content).block();
        cleaner.registerToBeDeleted(content); // in case of fails

        webTestClient.delete()
                .uri(ContentRest.CONTENT + ContentRest.PATH_SEG_ID_CID, content.getIdentity(), content.getContainerId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    content.getFileRefs().forEach(fileRef -> Assert.assertFalse(FileStorage.exist(fileRef.getIdentity())));
                    StepVerifier.create(repo.findById(content.getIdCid())).expectNextCount(0).verifyComplete();
                });
    }

    @Test
    public void testPagination() {
        Function<UUID, Content> supplier = (containerId) -> ContentMaker.largeRandom(containerId);
        pagination(10, 1, supplier);
        pagination(10, 3, supplier);
        pagination(10, 5, supplier);
        pagination(10, 11, supplier);
    }

    @Test
    public void nextUrl() {
        String value = "<http://127.0.0.1:38399/content?cid=b5a468c0-3264-11e9-8573-9352a7858433&page=2&size=3&ps=0018001010b5a86062326411e985739352a785843300f07ffffffc009a1b1e4a88b456bfc3fc6bdb87bba5bd0004>; rel=\"next\"";
        Assert.assertEquals("http://127.0.0.1:38399/content?cid=b5a468c0-3264-11e9-8573-9352a7858433&page=2&size=3&ps=0018001010b5a86062326411e985739352a785843300f07ffffffc009a1b1e4a88b456bfc3fc6bdb87bba5bd0004",
                LinkUtil.parseNextUrl(value));
    }

}
