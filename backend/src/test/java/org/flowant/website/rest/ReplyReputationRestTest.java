package org.flowant.website.rest;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReplyReputationRestTest extends RestWithRepositoryTest<ReplyReputation, IdCid, ReplyReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReplyReputationRest.PATH_REPLY_REPUTATION,
                ReplyReputation.class,
                ReplyReputation::getIdCid,
                ReputationMaker::emptyReplyReputation,
                ReputationMaker::randomReplyReputation,
                rr -> rr.setLiked(1));

        setDeleter(entity -> repo.deleteTestdataWithRelationship(entity).subscribe());
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
