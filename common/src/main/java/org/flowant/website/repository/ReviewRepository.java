package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReputationRepository<Review> {

    static final String UPDATE_REPUTATION = "UPDATE review SET reputation = ?2 " + 
            "WHERE identity = ?0 and containerId = ?1";

    @Query(UPDATE_REPUTATION)
    Mono<Object> updateReputationById(UUID identity, UUID containerId, Reputation reputation);

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid.getIdentity(), idCid.getContainerId(), reputation)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from review where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
