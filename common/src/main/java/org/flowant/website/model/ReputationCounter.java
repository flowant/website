package org.flowant.website.model;

public interface ReputationCounter extends HasIdCid {

    long getViewed();

    long getRated();

    long getLiked();

    long getDisliked();

    long getReported();

    long getReputed();

    default Reputation toReputation() {
        return Reputation.of(this);
    }

}
