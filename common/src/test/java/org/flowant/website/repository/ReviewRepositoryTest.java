package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Review;
import org.flowant.website.util.IdMaker;
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
    public void findAllByAuthorIdPageable() {
        UUID authorId = IdMaker.randomUUID();
        Flux<Review> entities = Flux.range(1, 10)
                .map(i -> ReviewMaker.largeRandom().setAuthorId(authorId)).cache();
        cleaner.registerToBeDeleted(entities);
        saveAndGetPaging(entities, pageable -> repo.findAllByAuthorId(authorId, pageable));
    }

    @Test
    public void findAllByIdCidContainerIdAndAuthorId() {
        UUID containerId = IdMaker.randomUUID();
        UUID authorId = IdMaker.randomUUID();
        Flux<Review> entities = Flux.range(1, 10)
                .map(i -> ReviewMaker.largeRandom(containerId).setAuthorId(authorId)).cache();
        cleaner.registerToBeDeleted(entities);
        saveAndGetPaging(entities, pageable -> repo.findAllByIdCidContainerIdAndAuthorId(containerId, authorId, pageable));
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
