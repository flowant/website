package org.flowant.website.rest;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.WebSiteConfig;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.model.Review;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReplyRestTest extends RestWithRepositoryTest<Reply, IdCid, ReplyRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReplyRest.REPLY, Reply.class, Reply::getIdCid,
                ReplyMaker::smallRandom, ReplyMaker::largeRandom,
                r -> r.setComment("newComment"));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testPagination() {
        Function<UUID, Reply> supplier = (containerId) -> ReplyMaker.largeRandom(containerId);
        pagination(10, 1, supplier);
        pagination(10, 3, supplier);
        pagination(10, 5, supplier);
        pagination(10, 11, supplier);
    }

    @Autowired
    ReplyReputationRepository repoRpt;

    @Test
    public void testPopularSubItem() {
        Function<Flux<Reply>, Flux<ReputationCounter>> save = entities -> {
            Flux<ReplyReputation> rpts = entities
                    .map(entity -> ReputationMaker.randomReplyReputation(entity.getIdCid())).cache();
            rpts.flatMap(repoRpt::save).blockLast();
            return rpts.cast(ReputationCounter.class);
        };

        popularSubItem(WebSiteConfig.getMaxSubItems(Review.class), ReplyMaker::largeRandom, save);
    }
}
