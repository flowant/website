package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.IdScore;
import org.flowant.website.model.Review;
import org.flowant.website.util.IdMaker;

public class ReviewMaker {

    static UUID reviewerId = IdMaker.randomUUID();
    static String reviewerName = "reviewerName";
    static String comment = "comment";

    static List<IdScore> popularSubItems = List.of(ReputationMaker.randomReplyReputation().toIdScore());

    public static Review small(IdCid idCid) {
        UUID id = idCid.getIdentity();
        return Review.of(idCid, reviewerId, reviewerName + id,
                ReputationMaker.randomReputation(), ReputationMaker.emptyReputation(), CRUZonedTime.now());
    }

    public static Review smallRandom(UUID containerId) {
        return small(IdCid.random(containerId));
    }

    public static Review smallRandom() {
        return small(IdCid.random());
    }

    public static Review large(IdCid idCid) {
        UUID id = idCid.getIdentity();
        Review review = small(idCid);
        review.setComment(comment + id);
        return review;
    }

    public static Review largeRandom(UUID containerId) {
        return large(IdCid.random(containerId));
    }

    public static Review largeRandom() {
        return large(IdCid.random());
    }

}
