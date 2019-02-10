package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewReputationRepositoryTest extends
        BaseRepositoryTest<ReviewReputation, ReviewReputationRepository> {

    @Test
    public void saveNewCounter() {
        ReviewReputation rr = ReviewReputation.of(UUID.randomUUID());
        deleteAfterTest(rr);

        Mono<ReviewReputation> saveThenFind = repo.save(rr).then(repo.findById(rr.getId()));
        StepVerifier.create(saveThenFind).expectNext(rr).verifyComplete();
    }

    @Test
    public void accumulateCounter() {
        ReviewReputation cr = ReviewReputation.of(UUID.randomUUID());
        deleteAfterTest(cr);
        ReviewReputation acc = ReviewReputation.of(cr.getId(), 1, 2, 3, 4);
        deleteAfterTest(acc);

        Mono<ReviewReputation> saveThenFind = repo.save(cr).then(repo.accumulate(acc)).then(repo.findById(cr.getId()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
