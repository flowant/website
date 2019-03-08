package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Message;
import org.flowant.website.util.test.MessageMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class MessageRepositoryTest extends IdCidRepositoryTest<Message, MessageRepository> {

    @Test
    public void crud() {
        testCrud(Message::getIdCid, MessageMaker::random, MessageMaker::random);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Message> entities = Flux.range(1, 10).map(i -> MessageMaker.random(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void ordered() {
        testOrdered(Message::getIdCid, Comparator.comparing(Message::getIdentity).reversed(),
                MessageMaker::random);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(MessageMaker.random(), Message::getContainerId);
    }

}
