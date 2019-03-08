package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Review;
import org.flowant.website.util.test.ReviewMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewRepositoryTest extends IdCidRepositoryTest<Review, ReviewRepository> {

    @Test
    public void crud() {
        testCrud(Review::getIdCid, ReviewMaker::smallRandom, ReviewMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Review> entities = Flux.range(1, 10).map(i -> ReviewMaker.smallRandom(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void ordered() {
        testOrdered(Review::getIdCid, Comparator.comparing(Review::getIdentity).reversed(),
                ReviewMaker::smallRandom);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReviewMaker.largeRandom(), Review::getContainerId);
    }

}
