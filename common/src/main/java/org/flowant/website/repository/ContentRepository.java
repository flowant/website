package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentRepository extends ReputationRepository<Content> {

    static final String UPDATE_REPUTATION = "UPDATE content SET reputation = ?2 " +
            "WHERE identity = ?0 and containerId = ?1";

    @Query(UPDATE_REPUTATION)
    Mono<Object> updateReputationById(UUID identity, UUID containerId, Reputation reputation);

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid.getIdentity(), idCid.getContainerId(), reputation)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from content where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

    default Mono<Void> deleteContentWithChildren(IdCid idCid) {
        return deleteById(idCid).then(RelationshipService.deleteChildren(idCid));
    }

    public static final String FIND_BY_TAG = "select * from content where tags contains ?0";

    @Query(FIND_BY_TAG)
    Flux<Content> findAllByTag(String tag);

    @Query(FIND_BY_TAG)
    Mono<Slice<Content>> findAllByTag(String tag, Pageable pageable);

}
