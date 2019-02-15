package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.ReplyReputation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyReputationRepositoryTest extends
        BaseRepositoryTest<ReplyReputation, UUID, ReplyReputationRepository> {

    @Test
    public void crud() {
        save(ReplyReputation.of(UUIDs.timeBased()), ReplyReputation::getId);
        save(ReplyReputation.of(UUIDs.timeBased(), 1, 2, 3, 4), ReplyReputation::getId);
    }

    @Test
    public void accumulateCounter() {
        ReplyReputation rr = ReplyReputation.of(UUIDs.timeBased());
        registerToBeDeleted(rr);
        ReplyReputation acc = ReplyReputation.of(rr.getId(), 1, 2, 3, 4);
        registerToBeDeleted(acc);

        Mono<ReplyReputation> saveThenFind = repo.save(rr).then(repo.accumulate(acc)).then(repo.findById(rr.getId()));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();
    }

}
