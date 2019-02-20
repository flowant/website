package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Reply;
import org.flowant.website.util.test.ReplyMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyRepositoryTest extends PageableRepositoryTest<Reply, ReplyRepository> {

    @Test
    public void crud() {
        testCrud(Reply::getMapId, ReplyMaker::smallRandom, ReplyMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Reply> entities = Flux.range(1, 10).map(i -> ReplyMaker.smallRandom().setContainerId(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void ordered() {
        testOrdered(Reply::getMapId, Comparator.comparing(Reply::getIdentity).reversed(),
                (id) -> ReplyMaker.smallRandom().setContainerId(id));
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReplyMaker.largeRandom(), Reply::getContainerId);
    }

}
