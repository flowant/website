package org.flowant.website.repository.devutil;

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.flowant.website.event.MockDataGenerateEvent;
import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.User;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.repository.RelationshipService;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.repository.ReviewRepository;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.repository.SubItemRepository;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.repository.WebSiteRepository;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class MockContentRepoUtil {

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

    Flux<Content> contents = Flux.empty();
    Flux<User> users = Flux.empty();

    int cntContents = 20;

    int cntUsers = 10;

    int cntRepliesPerReview = 3;

    public Content saveContent(Content content) {

        Mono<Content> con = Mono.just(content).cache();
        con.flatMap(repoContent::save).block();

        Mono<ContentReputation> contentReputation =
                con.map(c -> ReputationMaker.randomContentReputation(c.getIdCid())).cache();
        contentReputation.flatMap(repoContentRpt::save).block();

        // make one review per user at a content
        Flux<Review> reviews = users
                .map(user -> ReviewMaker.largeRandom(con.block().getIdentity())
                .setReviewerId(user.getIdentity()))
                .cache();
        reviews.flatMap(repoReview::save).blockLast();

        Flux<ReviewReputation> reviewReputations = reviews
                .map(r -> ReputationMaker.randomReviewReputation(r.getIdCid()))
                .cache();
        reviewReputations.flatMap(repoReviewRpt::save).blockLast();

        // make cntRepliesPerReview replies per review.
        Flux<Reply> replies = reviews
                .flatMap(review -> Flux.range(1, cntRepliesPerReview)
                .map(i -> ReplyMaker.largeRandom(review.getIdentity()))
                .cast(Reply.class))
                .cache();
        replies.flatMap(repoReply::save).blockLast();

        Flux<ReplyReputation> replyReputations = replies
                .map(r -> ReputationMaker.randomReplyReputation(r.getIdCid())).cache();
        replyReputations.flatMap(repoReplyRpt::save).blockLast();

        return con.block();
    }

    public void saveMockData() {

        UUID containerId = UUID.fromString("56a1cd50-3c77-11e9-bf26-d571c84212ed");

        users = Flux.range(1, cntUsers).map(i -> UserMaker.largeRandom()).cache();
        contents = Flux.range(1, cntContents).map(i -> ContentMaker.largeRandom(containerId)).cache();

        repoUser.saveAll(users).blockLast();
        contents.map(c -> saveContent(c)).blockLast();
    }

    @EventListener
    public void onApplicationEvent(MockDataGenerateEvent event) {
        log.debug(event::toString);
        saveMockData();
    }

    @PreDestroy
    public void onPreDestroy() throws Exception {

        contents.flatMap(c -> repoContent.deleteByIdWithRelationship(c.getIdCid())
                    .then(RelationshipService.deleteSubItemById(c.getContainerId())))
                    .blockLast();

        repoUser.deleteAll(users).block();

        log.debug("Mock data are deleted before shutting down.");
    }

}
