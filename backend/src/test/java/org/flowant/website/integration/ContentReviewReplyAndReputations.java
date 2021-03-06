package org.flowant.website.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.User;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
@Log4j2
public class ContentReviewReplyAndReputations extends BaseIntegrationTest {

    int cntContents = 10;
    int cntUsers = 3;
    int cntRepliesPerReview = 3;
    int pageSize = 3;

    @Test
    public void ContentAndReputations() {
        // Post test data
        UUID containerId = UUIDs.timeBased();

        Flux<Content> contents = Flux.range(1, cntContents)
                .map(i -> ContentMaker.largeRandom(containerId)).cache();
        Flux<ContentReputation> contentReputations = contents
                .map(c -> ReputationMaker.randomContentReputation(c.getIdCid())).cache();

        contents.subscribe(log::trace);
        contentReputations.subscribe(log::trace);

        // post all testcases instances
        contents.subscribe(postContent);
        contentReputations.subscribe(postContentReputation);

        // Get test data
        EntitiesAndNextLink<Content> resp = getPageByContainerId(containerId, Content.class, pageSize);
        log.trace(resp);

        // get sorted by reputation
        List<Content> popular = getPopularByContainerId(containerId, Content.class);
        log.trace("popular subItem:{}", popular);
    }

    @Test
    public void ReviewAndReputations() {

        // Post test data

        Mono<Content> content = Mono.just(ContentMaker.largeRandom()).cache();
        Mono<ContentReputation> contentReputation =
                content.map(c -> ReputationMaker.emptyContentReputation(c.getIdCid())).cache();

        Flux<User> users = Flux.range(1, cntUsers).map(i -> UserMaker.largeRandom()).cache();

        // make one review per user at a content
        Flux<Review> reviews = users
                .map(user -> ReviewMaker.largeRandom(content.block().getIdentity())
                .setAuthorId(user.getIdentity()))
                .cache();

        Flux<ReviewReputation> reviewReputations = reviews
                .map(r -> ReputationMaker.randomReviewReputation(r.getIdCid()))
                .cache();

        // make cntRepliesPerReview replies per review.
        Flux<Reply> replies = reviews
                .flatMap(review -> Flux.range(1, cntRepliesPerReview)
                .map(i -> ReplyMaker.largeRandom(review.getIdentity()))
                .cast(Reply.class))
                .cache();

        Flux<ReplyReputation> replyReputations = replies
                .map(r -> ReputationMaker.randomReplyReputation(r.getIdCid())).cache();

        content.subscribe(log::trace);
        contentReputation.subscribe(log::trace);
        users.subscribe(log::trace);
        reviews.subscribe(log::trace);
        reviewReputations.subscribe(log::trace);
        replies.subscribe(log::trace);
        replyReputations.subscribe(log::trace);

        // post all testcases instances
        content.subscribe(postContent);
        contentReputation.subscribe(postContentReputation);
        users.subscribe(postUser);
        reviews.subscribe(postReview);
        reviewReputations.subscribe(postReviewReputation);
        replies.subscribe(postReply);
        replyReputations.subscribe(postReplyReputation);

        // Reviews have reputing of content
        reviews.map(r -> ContentReputation.of(content.block().getIdCid(), r.getReputing()))
                .subscribe(postContentReputation);

        // Posting or putting some Reputation means accumulating Cassandra's counter values.
        reviews.map(r -> r.getReputing()).reduce((r1, r2) -> Reputation.of(r1, r2)).subscribe(r ->
            assertEquals(r.getLiked(), getById(content.block().getIdCid(), ContentReputation.class).getLiked()));

        // Get popular reviews
        List<Review> popular = getPopularByContainerId(content.block().getIdentity(), Review.class);
        log.trace("popular reviews: {}", popular);

        popular.forEach(review -> {
            List<Reply> popularReplies = getPopularByContainerId(review.getIdentity(), Reply.class);
            log.trace("popular replies: {}", popularReplies);
        });

    }
}
