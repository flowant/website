package org.flowant.website.repository;

import org.flowant.website.model.Content;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewReputationRepositoryTest extends ReputationRepositoryTest<ReviewReputation, ReviewReputationRepository> {

    @Before
    public void before() {
        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
    }

    @Test
    public void testAccumulation() {
        super.testAccumulation(ReviewReputation::getIdCid, ReviewReputation::of);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReputationMaker.randomReviewReputation(), ReviewReputation::getContainerId);
    }

    @Test
    public void testPopularChildren() {
        super.popularChildren(5, 10, ReputationMaker::randomReviewReputation, Content.class);
    }

}
