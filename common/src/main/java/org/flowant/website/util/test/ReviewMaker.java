package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Reputing;
import org.flowant.website.model.Review;

import com.datastax.driver.core.utils.UUIDs;

public class ReviewMaker {

    static UUID containerId = UUIDs.timeBased();
    static UUID reviewerId = UUIDs.timeBased();
    static String reviewerName = "reviewerName";
    static String comment = "comment";
    static List<UUID> popularReplyIds = List.of(UUIDs.timeBased(), UUIDs.timeBased());

    public static Review small(UUID id) {
        return Review.of(id, containerId, reviewerId, reviewerName + id,
                Reputing.of(1, 1, 1, 1), CRUZonedTime.now());
    }

    public static Review smallRandom() {
        return small(UUIDs.timeBased());
    }

    public static Review large(UUID id) {
        Review review = small(id);
        review.setComment(comment + id);
        review.setPopularReplyIds(popularReplyIds);
        return review;
    }

    public static Review largeRandom() {
        return large(UUIDs.timeBased());
    }

}
