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

public interface ContentRepository extends ReputationRepository<Content>, ReputationFragment<Content> {

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid, reputation, Content.class);
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
