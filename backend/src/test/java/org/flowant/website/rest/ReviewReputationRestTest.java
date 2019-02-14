package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.BackendReviewReputationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ReviewReputationRestTest extends BaseRestWithRepositoryTest<ReviewReputation, UUID, BackendReviewReputationRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ReviewReputationRest.REVIEW_REPUTATION, ReviewReputation.class, ReviewReputation::getId,
                () -> ReviewReputation.of(UUID.randomUUID()), () -> ReviewReputation.of(UUID.randomUUID(), 1, 2, 3, 4),
                (ReviewReputation cr) -> {
                    cr.setLiked(1);
                    return cr;
                });
    }

}
