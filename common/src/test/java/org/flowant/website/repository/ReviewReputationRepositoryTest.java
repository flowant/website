package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewReputationRepositoryTest extends
        BaseRepositoryTest<ReviewReputation, UUID, ReviewReputationRepository> {

    @Autowired
    ReviewRepository reviewRepo;

    @Test
    public void saveNewCounter() {
        ReviewReputation rr = ReviewReputation.of(UUID.randomUUID());
        registerToBeDeleted(rr);

        Mono<ReviewReputation> saveThenFind = repo.save(rr).then(repo.findById(rr.getId()));
        StepVerifier.create(saveThenFind).expectNext(rr).verifyComplete();
    }

    @Test
    public void accumulateCounter() {
        ReviewReputation rr = ReviewReputation.of(UUID.randomUUID());
        registerToBeDeleted(rr);
        ReviewReputation acc = ReviewReputation.of(rr.getId(), 1, 2, 3, 4);
        registerToBeDeleted(acc);

        Mono<ReviewReputation> saveThenFind = repo.save(rr).then(repo.accumulate(acc)).then(repo.findById(rr.getId()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
