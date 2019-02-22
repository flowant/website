package org.flowant.website.integration;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.repository.IdCidRepository;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.repository.ReviewRepository;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ContentMaker;
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
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
@Log4j2
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
    ContentReputationRepository repoContentRpt;
    @Autowired
    ReviewReputationRepository repoReviewRpt;
    @Autowired
    ReplyReputationRepository repoReplyRpt;
    @Autowired
    UserRepository repoUser;

    Flux<IdCidRepository<?>> repos;

    Flux<IdCid> toBeDeletedIdCids;

    @Before
    public void before() {
        repos = Flux.just(repoContent, repoReview, repoReply,
                repoContentRpt, repoReviewRpt, repoReplyRpt).cache();
    }

    @After
    public void after() {
        repos.subscribe(repo -> toBeDeletedIdCids.flatMap(repo::deleteById).subscribe());
    }

    @Test
    public void deleteChildren() {
        IdCid mapId = IdCid.random();
        toBeDeletedIdCids = Flux.just(mapId);

        repoContent.save(ContentMaker.large(mapId)).block();
        repoReview.save(ReviewMaker.large(mapId)).block();
        repoReply.save(ReplyMaker.large(mapId)).block();
        repoContentRpt.save(ReputationMaker.randomContentReputation(mapId)).block();
        repoReviewRpt.save(ReputationMaker.randomReviewReputation(mapId)).block();
        repoReplyRpt.save(ReputationMaker.randomReplyReputation(mapId)).block();

        repos.doOnNext(repo -> StepVerifier.create(repo.findById(mapId)).expectNextCount(1)
                .verifyComplete()).blockLast();

        repoContent.deleteContentWithChildren(mapId).block();

        repos.doOnNext(repo -> StepVerifier.create(repo.findById(mapId)).expectNextCount(0)
                .verifyComplete()).blockLast();
    }

    @Test
    public void sortByReputation() {
        UUID containerId = IdMaker.randomUUID();
        Flux<Content> contents = Flux.range(1, 10).map(i -> ContentMaker.largeRandom(containerId)).cache();
        toBeDeletedIdCids = contents.map(Content::getIdCid);
        repoContent.saveAll(contents).blockLast();

        Flux<ContentReputation> reputations = contents.map(c -> ReputationMaker.randomContentReputation(c.getIdCid())).cache();
        reputations.flatMap(repoContentRpt::save).blockLast();

        log.trace("repoContent:");
        repoContent.findAllByIdCidContainerId(containerId)
                .sort(Comparator.comparing(Content::getReputation).reversed())
                .doOnNext(log::trace).blockLast();

        log.trace("repoContentRpt");
        Flux<IdCid> ids = repoContentRpt.findAllByIdCidContainerId(containerId)
                .sort(Comparator.comparing(ContentReputation::getLiked).reversed())
                .doOnNext(log::trace).map(ContentReputation::getIdCid);

        // Sequences of Publisher and results are not the same.
        // we cannot get sorted content using sorted Publisher
        repoContent.findAllById(ids).doOnNext(log::trace).blockLast();
    }
}
