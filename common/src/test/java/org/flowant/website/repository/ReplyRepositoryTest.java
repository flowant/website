package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Reply;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyRepositoryTest extends IdCidRepositoryTest<Reply, ReplyRepository> {

    @Test
    public void crud() {
        testCrud(Reply::getIdCid, ReplyMaker::smallRandom, ReplyMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Reply> entities = Flux.range(1, 10).map(i -> ReplyMaker.smallRandom(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void findAllByAuthorIdPageable() {
        UUID authorId = IdMaker.randomUUID();
        Flux<Reply> entities = Flux.range(1, 10)
                .map(i -> ReplyMaker.largeRandom().setAuthorId(authorId)).cache();
        cleaner.registerToBeDeleted(entities);
        saveAndGetPaging(entities, pageable -> repo.findAllByAuthorId(authorId, pageable));
    }

    @Test
    public void ordered() {
        testOrdered(Reply::getIdCid, Comparator.comparing(Reply::getIdentity).reversed(),
                ReplyMaker::smallRandom);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReplyMaker.largeRandom(), Reply::getContainerId);
    }

}
