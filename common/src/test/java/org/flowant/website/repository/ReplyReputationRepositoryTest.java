package org.flowant.website.repository;

import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyReputationRepositoryTest extends ReputationRepositoryTest<ReplyReputation, ReplyReputationRepository> {

    @Before
    public void before() {
        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
    }

    @Test
    public void testAccumulation() {
        super.testAccumulation(ReplyReputation::getIdCid, ReplyReputation::of);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReputationMaker.randomReplyReputation(), ReplyReputation::getContainerId);
    }

    @Test
    public void testPopularChildren() {
        super.popularChildren(5, 10, ReputationMaker::randomReplyReputation, Review.class);
    }

}
