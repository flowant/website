package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ContentReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentReputationRepositoryTest extends
        BaseRepositoryTest<ContentReputation, UUID, ContentReputationRepository> {

    @Test
    public void saveNewCounter() {
        ContentReputation cr = ContentReputation.of(UUID.randomUUID());
        deleteAfterTest(cr);

        Mono<ContentReputation> saveThenFind = repo.save(cr).then(repo.findById(cr.getId()));
        StepVerifier.create(saveThenFind).expectNext(cr).verifyComplete();
    }

    @Test
    public void accumulateCounter() {
        ContentReputation cr = ContentReputation.of(UUID.randomUUID());
        deleteAfterTest(cr);
        ContentReputation acc = ContentReputation.of(cr.getId(), 1, 2, 3, 4, 5, 6);
        deleteAfterTest(acc);

        Mono<ContentReputation> saveThenFind = repo.save(cr).then(repo.accumulate(acc)).then(repo.findById(cr.getId()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
