package org.flowant.website.integration;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.User;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
@Log4j2
public class ContentReviewReplyAndReputations extends BaseIntegrationTest {

    int cntUsers = 3;
    int cntRepliesPerReview = 3;

    @Test
    public void ContentReviewAndReputations() {

        Mono<Content> content = Mono.just(ContentMaker.largeRandom()).cache();
        Mono<ContentReputation> contentReputation = content.map(c -> ContentReputation.of(c.getId())).cache();

        Flux<User> users = Flux.range(1, cntUsers).map(i -> UserMaker.largeRandom()).cache();

        // make 1 reviews per users at a content
        Flux<Review> reviews = users.map(user -> ReviewMaker.largeRandom()
                .setReviewerId(user.getId())
                .setContainerId(content.block().getId())).cache();

        Flux<ReviewReputation> reviewReputations = reviews.map(review ->
            ReviewReputation.of(review.getId())).cache();

        // make cntRepliesPerReview replies per review.
        Flux<Reply> replies = reviews.flatMap(review ->
            Flux.range(1, cntRepliesPerReview)
                .map(i -> ReplyMaker.largeRandom().setContainerId(review.getId()))
                .cast(Reply.class)).cache();

        Flux<ReplyReputation> replyReputations = replies.map(reply ->
            ReplyReputation.of(reply.getId())).cache();

        log.trace(content);
        log.trace(contentReputation);
        users.subscribe(log::trace);
        reviews.subscribe(log::trace);
        reviewReputations.subscribe(log::trace);
        replies.subscribe(log::trace);
        replyReputations.subscribe(log::trace);

    }
}
