package org.flowant.website.rest;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReviewReputationRestTest extends RestWithRepositoryTest<ReviewReputation, IdCid, ReviewReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReviewReputationRest.REVIEW_REPUTATION,
                ReviewReputation.class,
                ReviewReputation::getIdCid,
                ReputationMaker::emptyReviewReputation,
                ReputationMaker::randomReviewReputation,
                rr -> rr.setLiked(1));

        setDeleter(entity -> repo.deleteTestdataWithRelationship(entity).subscribe());
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
