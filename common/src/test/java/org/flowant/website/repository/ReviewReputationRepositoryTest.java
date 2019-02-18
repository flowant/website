package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReviewReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewReputationRepositoryTest extends
        RepositoryTest<ReviewReputation, UUID, ReviewReputationRepository> {

    @Test
    public void crud() {
        save(ReviewReputation.of(UUIDs.timeBased()), ReviewReputation::getIdentity);
        save(ReviewReputation.of(UUIDs.timeBased(), 1, 2, 3, 4), ReviewReputation::getIdentity);
    }

    @Test
    public void accumulateCounter() {
        ReviewReputation rr = ReviewReputation.of(UUIDs.timeBased());
        registerToBeDeleted(rr);
        ReviewReputation acc = ReviewReputation.of(rr.getIdentity(), 1, 2, 3, 4);
        registerToBeDeleted(acc);

        Mono<ReviewReputation> saveThenFind = repo.save(rr).then(repo.accumulate(acc))
                .then(repo.findById(rr.getIdentity()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
