package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Review;
import org.flowant.website.repository.BackendReviewRepository;
import org.flowant.website.util.test.ReviewMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ReviewRestTest extends BaseRestWithRepositoryTest<Review, UUID, BackendReviewRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ReviewRest.REVIEW, Review.class, Review::getId,
                ReviewMaker::smallRandom, ReviewMaker::largeRandom,
                (Review r) -> {
                    r.setComment("newComment");
                    return r;}
                );
    }

}
