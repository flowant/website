package org.flowant.website.integration;

import org.flowant.website.repository.ContentRepository;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.repository.MapIdRepository;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.repository.ReviewRepository;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.IdMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class IntegrationTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    ContentRepository repoContent;
    @Autowired
    ReviewRepository repoReview;
    @Autowired
    ReplyRepository repoReply;
    @Autowired
    ContentReputationRepository repoContentReputation;
    @Autowired
    ReviewReputationRepository repoReviewReputation;
    @Autowired
    ReplyReputationRepository repoReplyReputation;
    @Autowired
    UserRepository repoUser;

    Flux<MapIdRepository<?>> repos;

    MapId toBeDeletedMapId;

    @Before
    public void before() {
        repos = Flux.just(repoContent, repoReview, repoReply,
                repoContentReputation, repoReviewReputation, repoReplyReputation).cache();
    }

    @After
    public void after() {
        repos.subscribe(repo -> repo.deleteById(toBeDeletedMapId).subscribe());
    }

    @Test
    public void testDeleteChildren() {
        deleteChildren();
    }

    public void deleteChildren() {

        MapId mapId = IdMaker.randomMapId();
        toBeDeletedMapId = mapId;

        repoContent.save(ContentMaker.large(mapId)).block();
        repoReview.save(ReviewMaker.large(mapId)).block();
        repoReply.save(ReplyMaker.large(mapId)).block();
        repoContentReputation.save(ReputationMaker.randomContentReputation(mapId)).block();
        repoReviewReputation.save(ReputationMaker.randomReviewReputation(mapId)).block();
        repoReplyReputation.save(ReputationMaker.randomReplyReputation(mapId)).block();

        repos.doOnNext(repo -> StepVerifier.create(repo.findById(mapId)).expectNextCount(1)
                .verifyComplete()).blockLast();

        repoContent.deleteContentWithChildren(mapId).block();

        repos.doOnNext(repo -> StepVerifier.create(repo.findById(mapId)).expectNextCount(0)
                .verifyComplete()).blockLast();
    }

}
