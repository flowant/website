package org.flowant.website.rest;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Reply;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.util.test.ReplyMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReplyRestTest extends RestWithRepositoryTest<Reply, MapId, ReplyRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReplyRest.REPLY, Reply.class, Reply::getMapId,
                ReplyMaker::smallRandom, ReplyMaker::largeRandom,
                r -> r.setComment("newComment"));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testPagination() {
        Function<UUID, Reply> supplier = (containerId) -> ReplyMaker.largeRandom().setContainerId(containerId);
        pagination(10, 1, supplier);
        pagination(10, 3, supplier);
        pagination(10, 5, supplier);
        pagination(10, 11, supplier);
    }
}
