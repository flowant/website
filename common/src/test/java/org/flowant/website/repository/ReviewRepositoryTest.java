package org.flowant.website.repository;

import org.flowant.website.model.Review;
import org.flowant.website.util.test.ReviewMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewRepositoryTest extends BaseRepositoryTest<Review, ReviewRepository> {

    @Test
    public void crud() {
        testCrud(ReviewMaker::smallRandom, ReviewMaker::largeRandom);
    }
}
