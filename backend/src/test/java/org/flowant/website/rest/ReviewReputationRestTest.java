package org.flowant.website.rest;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.BackendReviewReputationRepository;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReviewReputationRestTest extends RestWithRepositoryTest<ReviewReputation, MapId, BackendReviewReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReviewReputationRest.REVIEW_REPUTATION,
                ReviewReputation.class,
                ReviewReputation::getMapId,
                ReputationMaker::emptyReviewReputation,
                ReputationMaker::randomReviewReputation,
                rr -> rr.setLiked(1));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
