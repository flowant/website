package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.springframework.data.cassandra.core.mapping.MapId;

public class ReviewMaker {

    static UUID reviewerId = IdMaker.randomUUID();
    static String reviewerName = "reviewerName";
    static String comment = "comment";
    static List<UUID> popularReplyIds = List.of(IdMaker.randomUUID(), IdMaker.randomUUID());

    public static Review small(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        return Review.of(id, IdMaker.toContainerId(mapId), reviewerId, reviewerName + id,
                ReputationMaker.randomReputation(), CRUZonedTime.now());
    }

    public static Review smallRandom() {
        return small(IdMaker.randomMapId());
    }

    public static Review large(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        Review review = small(mapId);
        review.setComment(comment + id);
        review.setPopularReplyIds(popularReplyIds);
        return review;
    }

    public static Review largeRandom() {
        return large(IdMaker.randomMapId());
    }

}
