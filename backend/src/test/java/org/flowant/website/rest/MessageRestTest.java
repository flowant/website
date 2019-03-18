package org.flowant.website.rest;

import static org.flowant.website.rest.IdCidRepositoryRest.AID;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Message;
import org.flowant.website.repository.MessageRepository;
import org.flowant.website.util.test.MessageMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class MessageRestTest extends RestWithRepositoryTest<Message, IdCid, MessageRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(MessageRest.MESSAGE, Message.class, Message::getIdCid,
                MessageMaker::random, MessageMaker::random,
                r -> r.setSentences("new sentences"));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testPagination() {
        Function<UUID, Message> supplier = (containerId) -> MessageMaker.random(containerId);
        pagination(10, 1, supplier);
        pagination(10, 3, supplier);
        pagination(10, 5, supplier);
        pagination(10, 11, supplier);
    }

    @Test
    public void testPaginationByAuthorId() {
        Function<UUID, Message> supplier = (authorId) -> MessageMaker.random().setAuthorId(authorId);

        pagination(10, 1, AID, supplier);
        pagination(10, 3, AID, supplier);
        pagination(10, 5, AID, supplier);
        pagination(10, 11, AID, supplier);
    }

}
