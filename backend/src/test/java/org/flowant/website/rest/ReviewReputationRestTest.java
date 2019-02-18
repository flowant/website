package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.BackendReviewReputationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ReviewReputationRestTest extends RestWithRepositoryTest<ReviewReputation, UUID, BackendReviewReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReviewReputationRest.REVIEW_REPUTATION, ReviewReputation.class, ReviewReputation::getIdentity,
                () -> ReviewReputation.of(UUIDs.timeBased()), () -> ReviewReputation.of(UUIDs.timeBased(), 1, 2, 3, 4),
                (ReviewReputation cr) -> {
                    cr.setLiked(1);
                    return cr;
                });
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
