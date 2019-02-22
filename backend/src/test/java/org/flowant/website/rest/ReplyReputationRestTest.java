package org.flowant.website.rest;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.repository.BackendReplyReputationRepository;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReplyReputationRestTest extends RestWithRepositoryTest<ReplyReputation, MapId, BackendReplyReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReplyReputationRest.REPLY_REPUTATION,
                ReplyReputation.class,
                ReplyReputation::getMapId,
                ReputationMaker::emptyReplyReputation,
                ReputationMaker::randomReplyReputation,
                rr -> rr.setLiked(1));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
