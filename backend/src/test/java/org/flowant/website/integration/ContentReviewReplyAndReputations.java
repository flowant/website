package org.flowant.website.integration;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputing;
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

    int cntContents = 3;
    int cntUsers = 3;
    int cntRepliesPerReview = 3;
    int pageSize = 3;

    @Test
    public void ContentAndReputaations() {
        // Post test data
        UUID containerId = UUIDs.timeBased();

        Flux<Content> contents = Flux.range(1, cntUsers).map(i -> ContentMaker.largeRandom().setContainerId(containerId)).cache();
        Flux<ContentReputation> contentReputations = contents.map(c -> ReputationMaker.randomContentReputation(c.getIdentity())).cache();
        contents.subscribe(log::trace);
        contentReputations.subscribe(log::trace);

        // post all testcases instances
        contents.subscribe(postContent);
        contentReputations.subscribe(postContentReputation);

        // Get test data
        EntitiesAndNextLink<Content> resp = getPageByContainerId(containerId, Content.class, pageSize);
        log.trace(resp);
//        resp.getEntities().map(Content::getId)
        // get sorted by reputation

    }

    @Test
    public void ReviewAndReputations() {

        // Post test data

        Mono<Content> content = Mono.just(ContentMaker.largeRandom()).cache();
        Mono<ContentReputation> contentReputation = content.map(c -> ContentReputation.of(c.getIdentity())).cache();

        Flux<User> users = Flux.range(1, cntUsers).map(i -> UserMaker.largeRandom()).cache();

        // make one review per users at a content
        Flux<Review> reviews = users.map(user -> ReviewMaker.largeRandom()
                .setReviewerId(user.getIdentity())
                .setContainerId(content.block().getIdentity())).cache();

        Flux<ReviewReputation> reviewReputations = reviews.map(review ->
                ReputationMaker.randomReviewReputation(review.getIdentity())).cache();

        // make cntRepliesPerReview replies per review.
        Flux<Reply> replies = reviews.flatMap(review ->
            Flux.range(1, cntRepliesPerReview)
                .map(i -> ReplyMaker.largeRandom().setContainerId(review.getIdentity()))
                .cast(Reply.class)).cache();

        Flux<ReplyReputation> replyReputations = replies.map(reply ->
                ReputationMaker.randomReplyReputation(reply.getIdentity())).cache();

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
        reviews.map(r -> ReputationMaker.toContentReputation(r.getContainerId(), r.getReputing()))
                .subscribe(postContentReputation);

        // Posting or putting some Reputation means accumulating Cassandra's counter values.
        reviews.map(r -> r.getReputing()).reduce((r1, r2) -> Reputing.of(r1, r2)).subscribe(r ->
            assertEquals(r.getLiking(), getById(content.block().getMapId(), ContentReputation.class).getLiked()));

        // Get test data

        // listing review and reviewReputataion

    }
}
