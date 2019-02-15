package org.flowant.website.util.test;

import java.util.Random;
import java.util.UUID;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputing;
import org.flowant.website.model.ReviewReputation;

import com.datastax.driver.core.utils.UUIDs;

public class ReputationMaker {
    static Random r = new Random();

    public static ContentReputation toContentReputation(UUID id, Reputing reputing) {
        return ContentReputation.of(id, 1, reputing.getRating(), reputing.getLiking(),
                reputing.getDisliking(), reputing.getReporting(), 1);
    }

    public static ContentReputation randomContentReputation() {
        return randomContentReputation(UUIDs.timeBased());
    }

    public static ContentReputation randomContentReputation(UUID id) {
        return ContentReputation.of(id, r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong());
    }

    public static ReviewReputation randomReviewReputation() {
        return randomReviewReputation(UUIDs.timeBased());
    }

    public static ReviewReputation randomReviewReputation(UUID id) {
        return ReviewReputation.of(id, r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong());
    }

    public static ReplyReputation randomReplyReputation() {
        return randomReplyReputation(UUIDs.timeBased());
    }

    public static ReplyReputation randomReplyReputation(UUID id) {
        return ReplyReputation.of(id, r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong());
    }
}
