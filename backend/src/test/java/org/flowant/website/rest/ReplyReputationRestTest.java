package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.repository.BackendReplyReputationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ReplyReputationRestTest extends BaseRestWithRepositoryTest<ReplyReputation, UUID, BackendReplyReputationRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ReplyReputationRest.REPLY_REPUTATION, ReplyReputation.class, ReplyReputation::getId,
                () -> ReplyReputation.of(UUID.randomUUID()), () -> ReplyReputation.of(UUID.randomUUID(), 1, 2, 3, 4),
                (ReplyReputation cr) -> {
                    cr.setLiked(1);
                    return cr;
                });
    }

}
