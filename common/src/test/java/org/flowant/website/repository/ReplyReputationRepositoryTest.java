package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyReputationRepositoryTest extends
        BaseRepositoryTest<ReplyReputation, ReplyReputationRepository> {

    @Test
    public void saveNewCounter() {
        ReplyReputation rr = ReplyReputation.of(UUID.randomUUID());
        deleteAfterTest(rr);

        Mono<ReplyReputation> saveThenFind = repo.save(rr).then(repo.findById(rr.getId()));
        StepVerifier.create(saveThenFind).expectNext(rr).verifyComplete();
    }

    @Test
    public void accumulateCounter() {
        ReplyReputation cr = ReplyReputation.of(UUID.randomUUID());
        deleteAfterTest(cr);
        ReplyReputation acc = ReplyReputation.of(cr.getId(), 1, 2, 3, 4);
        deleteAfterTest(acc);

        Mono<ReplyReputation> saveThenFind = repo.save(cr).then(repo.accumulate(acc)).then(repo.findById(cr.getId()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
