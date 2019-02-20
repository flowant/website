package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentRepositoryTest extends PageableRepositoryTest<Content, MapId, ContentRepository> {

    @Test
    public void crud() {
        testCrud(Content::getMapId, ContentMaker::smallRandom, ContentMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Content> entities = Flux.range(1, 10).map(i -> ContentMaker.smallRandom().setContainerId(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void ordered() {
        testOrdered(Content::getMapId, Comparator.comparing(Content::getIdentity).reversed(),
                (id) -> ContentMaker.smallRandom().setContainerId(id));
    }

}
