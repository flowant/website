package org.flowant.website.model;

public interface ReputationCounter extends HasIdCid, Comparable<ReputationCounter>{

    long getViewed();

    long getRated();

    long getLiked();

    long getDisliked();

    long getReported();

    long getReputed();

    default Reputation toReputation() {
        return Reputation.of(this);
    }

    default public int compareTo(ReputationCounter counter) {
        return Long.compare(getLiked(), counter.getLiked());
    }

}
