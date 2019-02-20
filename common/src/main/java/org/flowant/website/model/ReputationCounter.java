package org.flowant.website.model;

public interface ReputationCounter extends HasMapId {

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
