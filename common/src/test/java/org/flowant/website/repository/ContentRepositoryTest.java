package org.flowant.website.repository;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentRepositoryTest extends IdCidRepositoryTest<Content, ContentRepository> {

    @Test
    public void crud() {
        testCrud(Content::getIdCid, ContentMaker::smallRandom, ContentMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Content> entities = Flux.range(1, 10).map(i -> ContentMaker.smallRandom(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void findAllByAuthorIdPageable() {
        UUID authorId = IdMaker.randomUUID();
        Flux<Content> entities = Flux.range(1, 10)
                .map(i -> ContentMaker.largeRandom().setAuthorId(authorId)).cache();
        cleaner.registerToBeDeleted(entities);
        saveAndGetPaging(entities, pageable -> repo.findAllByAuthorId(authorId, pageable));
    }

    @Test
    public void ordered() {
        testOrdered(Content::getIdCid, Comparator.comparing(Content::getIdentity).reversed(),
                ContentMaker::smallRandom);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ContentMaker.largeRandom(), Content::getContainerId);
    }

    @Test
    public void findByTag() {
        String tag = IdMaker.randomUUID().toString();
        Flux<Content> withTag = Flux.range(1, 5)
                .map(i -> ContentMaker.largeRandom().setTags(Set.of(tag))).cache();
        cleaner.registerToBeDeleted(withTag);

        Flux<Content> withoutTag = Flux.range(1, 5).map(i -> ContentMaker.largeRandom()).cache();
        cleaner.registerToBeDeleted(withoutTag);

        repo.saveAll(withTag).blockLast();
        repo.saveAll(withoutTag).blockLast();

        assertEquals(withTag.collectSortedList().block(),
                repo.findAllByTag(tag).collectSortedList().block());
    }

    @Test
    public void findAllByTagPageable() {
        String tag = IdMaker.randomUUID().toString();
        Flux<Content> contents = Flux.range(1, 10)
                .map(i -> ContentMaker.largeRandom().setTags(Set.of(tag))).cache();
        cleaner.registerToBeDeleted(contents);
        saveAndGetPaging(contents, pageable -> repo.findAllByTag(tag, pageable));
    }

}
