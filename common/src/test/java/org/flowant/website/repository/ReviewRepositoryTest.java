package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Review;
import org.flowant.website.util.test.ReviewMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewRepositoryTest extends BaseRepositoryTest<Review, UUID, ReviewRepository> {

    @Test
    public void crud() {
        testCrud(Review::getId, ReviewMaker::smallRandom, ReviewMaker::largeRandom);
    }

}
