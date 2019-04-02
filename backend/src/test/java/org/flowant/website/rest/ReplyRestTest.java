package org.flowant.website.rest;

import static org.flowant.website.rest.IdCidRepositoryRest.AID;
import static org.flowant.website.rest.IdCidRepositoryRest.CID;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.WebSiteConfig;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.model.Review;
import org.flowant.website.repository.RelationshipService;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ReplyRestTest extends RestWithRepositoryTest<Reply, IdCid, ReplyRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ReplyRest.PATH_REPLY, Reply.class, Reply::getIdCid,
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

    @Test
    public void testPaginationByAuthorId() {
        Function<UUID, Reply> supplier = (authorId) -> ReplyMaker.largeRandom().setAuthorId(authorId);

        pagination(10, 1, AID, supplier);
        pagination(10, 3, AID, supplier);
        pagination(10, 5, AID, supplier);
        pagination(10, 11, AID, supplier);
    }

    @Test
    public void testPaginationByContainerIdAndAuthorId() {

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(AID, IdMaker.randomUUID().toString());
        multiValueMap.add(CID, IdMaker.randomUUID().toString());

        Function<MultiValueMap<String, String>, Reply> supplier =
                (params) -> ReplyMaker.largeRandom(UUID.fromString(params.getFirst(CID)))
                                      .setAuthorId(UUID.fromString(params.getFirst(AID)));

        pagination(10, 1, multiValueMap, supplier);
        pagination(10, 3, multiValueMap, supplier);
        pagination(10, 5, multiValueMap, supplier);
        pagination(10, 11, multiValueMap, supplier);
    }

    @Autowired
    ReplyReputationRepository repoRpt;

    @Test
    public void testPopularSubItem() {

        setDeleter(entity -> repo.deleteByIdWithRelationship(entity.getIdCid())
                .then(RelationshipService.deleteSubItemById(entity.getContainerId()))
                .subscribe());

        Function<Flux<Reply>, Flux<ReputationCounter>> save = entities -> {
            Flux<ReplyReputation> rpts = entities
                    .map(entity -> ReputationMaker.randomReplyReputation(entity.getIdCid())).cache();
            rpts.flatMap(repoRpt::save).blockLast();
            return rpts.cast(ReputationCounter.class);
        };

        popularSubItem(WebSiteConfig.getMaxSubItems(Review.class), ReplyMaker::largeRandom, save);
    }
}
