package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Review;

public class ReviewMaker {

    static UUID containerId = UUID.randomUUID();
    static UUID reviewerId = UUID.randomUUID();
    static String reviewerName = "reviewerName";
    static String comment = "comment";
    static List<UUID> popularReplyIds = List.of(UUID.randomUUID(), UUID.randomUUID());

    public static Review small(UUID id) {
        return Review.of(id, containerId, reviewerId, reviewerName + id, CRUZonedTime.now());
    }

    public static Review smallRandom() {
        return small(UUID.randomUUID());
    }

    public static Review large(UUID id) {
        Review review = small(id);
        review.setComment(comment + id);
        review.setPopularReplyIds(popularReplyIds);
        return review;
    }

    public static Review largeRandom() {
        return large(UUID.randomUUID());
    }

}
