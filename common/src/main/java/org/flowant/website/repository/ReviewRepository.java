package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReputationRepository<Review>, ReputationFragment<Review> {

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid, reputation, Review.class)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from review where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

    default Mono<Void> deleteByIdWithRelationship(IdCid idCid) {
        UUID identity = idCid.getIdentity();
        return deleteById(idCid)
                .then(RelationshipService.deleteCounter(idCid, Review.class))
                .then(RelationshipService.deleteSubItemById(identity))
                .then(RelationshipService.deleteCounterByContainerId(identity, Reply.class))
                .then(RelationshipService.deleteReputationByContainerId(identity, ReplyReputation.class));
    }

}
