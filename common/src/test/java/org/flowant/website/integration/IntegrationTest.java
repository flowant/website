package org.flowant.website.integration;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.repository.IdCidRepository;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.repository.ReviewRepository;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.repository.SubItemRepository;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.repository.WebSiteRepository;
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
    WebSiteRepository repoWebSite;

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
    SubItemRepository repoSubItem;

    @Autowired
    UserRepository repoUser;

    Flux<IdCidRepository<?>> repos;

    Flux<IdCid> toBeDeletedIdCids = Flux.empty();
    Flux<UUID> toBeDeletedPopularSubItems = Flux.empty();

    @Before
    public void before() {
        repos = Flux.just(repoContent, repoReview, repoReply,
                repoContentRpt, repoReviewRpt, repoReplyRpt).cache();
    }

    @After
    public void after() {
        repos.subscribe(repo -> toBeDeletedIdCids.flatMap(repo::deleteById).subscribe());
        toBeDeletedPopularSubItems.flatMap(repoSubItem::deleteById).blockLast();
    }

    @Test
    public void replyDeleteByIdWithRelationship() {
        IdCid idCid = IdCid.random();

        Reply reply = ReplyMaker.large(idCid);
        ReplyReputation replyR = ReputationMaker.randomReplyReputation(reply.getIdCid());

        repoReply.save(reply).block();
        repoReplyRpt.save(replyR).block();

        StepVerifier.create(repoReply.findById(idCid)).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(idCid)).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(idCid.getContainerId())).expectNextCount(1).verifyComplete();

        repoReply.deleteByIdWithRelationship(idCid).block();

        StepVerifier.create(repoReply.findById(idCid)).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(idCid)).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoSubItem.findById(idCid.getContainerId())).expectNextCount(1).verifyComplete();

        toBeDeletedPopularSubItems = Flux.just(idCid.getContainerId());
    }

    @Test
    public void reviewDeleteByIdWithRelationship() {
        IdCid idCid = IdCid.random();

        Review review = ReviewMaker.large(idCid);
        Reply reply = ReplyMaker.large(IdCid.random(review.getIdentity()));

        ReviewReputation reviewR = ReputationMaker.randomReviewReputation(review.getIdCid());
        ReplyReputation replyR = ReputationMaker.randomReplyReputation(reply.getIdCid());

        repoReview.save(review).block();
        repoReply.save(reply).block();
        repoReviewRpt.save(reviewR).block();
        repoReplyRpt.save(replyR).block();

        StepVerifier.create(repoReview.findById(review.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReply.findById(reply.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReviewRpt.findById(review.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(reply.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(review.getContainerId())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(reply.getContainerId())).expectNextCount(1).verifyComplete();

        repoReview.deleteByIdWithRelationship(idCid).block();

        StepVerifier.create(repoReview.findById(review.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReply.findById(reply.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReviewRpt.findById(review.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(reply.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoSubItem.findById(review.getContainerId())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(reply.getContainerId())).expectNextCount(0).verifyComplete();

        toBeDeletedPopularSubItems = Flux.just(idCid.getContainerId());
    }

    @Test
    public void contentDeleteByIdWithRelationship() {
        IdCid idCid = IdCid.random();

        Content content = ContentMaker.large(idCid);
        Review review = ReviewMaker.large(IdCid.random(content.getIdentity()));
        Reply reply = ReplyMaker.large(IdCid.random(review.getIdentity()));

        ContentReputation contentR = ReputationMaker.randomContentReputation(content.getIdCid());
        ReviewReputation reviewR = ReputationMaker.randomReviewReputation(review.getIdCid());
        ReplyReputation replyR = ReputationMaker.randomReplyReputation(reply.getIdCid());

        repoContent.save(content).block();
        repoReview.save(review).block();
        repoReply.save(reply).block();
        repoContentRpt.save(contentR).block();
        repoReviewRpt.save(reviewR).block();
        repoReplyRpt.save(replyR).block();

        StepVerifier.create(repoContent.findById(content.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReview.findById(review.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReply.findById(reply.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoContentRpt.findById(content.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReviewRpt.findById(review.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(reply.getIdCid())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(content.getContainerId())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(review.getContainerId())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(reply.getContainerId())).expectNextCount(1).verifyComplete();

        repoContent.deleteByIdWithRelationship(idCid).block();

        StepVerifier.create(repoContent.findById(content.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReview.findById(review.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReply.findById(reply.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoContentRpt.findById(content.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReviewRpt.findById(review.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoReplyRpt.findById(reply.getIdCid())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoSubItem.findById(content.getContainerId())).expectNextCount(1).verifyComplete();
        StepVerifier.create(repoSubItem.findById(review.getContainerId())).expectNextCount(0).verifyComplete();
        StepVerifier.create(repoSubItem.findById(reply.getContainerId())).expectNextCount(0).verifyComplete();

        toBeDeletedPopularSubItems = Flux.just(content.getContainerId());
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

        toBeDeletedPopularSubItems = Flux.just(containerId);
    }

}
