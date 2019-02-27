package org.flowant.website.model;

import org.flowant.website.util.NumberUtil;

public interface ReputationCounter extends HasIdCid {

    int THRESHOLD = 15;

    long getViewed();

    long getRated();

    long getLiked();

    long getDisliked();

    long getReported();

    long getReputed();

    default Reputation toReputation() {
        return Reputation.of(this);
    }

    public static int compare(ReputationCounter first, ReputationCounter second) {
        return Long.compare(first.toScore(), second.toScore());
    }

    public static long toScore(long rated, long liked, long disliked, long reported) {
        // TODO
        double score = liked;
        return (long) score;
    }

    default public long toScore() {
        return toScore(getRated(), getLiked(), getDisliked(), getReported());
    }

    default public IdScore toIdScore() {
        return IdScore.of(getIdentity(), toScore());
    }

    default public boolean isOverThreshold() {
        long score = toScore();
        // TODO
        return score > THRESHOLD && NumberUtil.isPowerOfTwo(score);
    }

}
