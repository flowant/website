package org.flowant.website.repository;

import static org.flowant.website.model.HasMapId.CONTAINER_ID;
import static org.flowant.website.model.HasMapId.IDENTITY;

import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.Reputation;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentRepository extends PageableRepository<Content> {

    static final String UPDATE_REPUTATION = "UPDATE content SET reputation = ?2 " +
            "WHERE identity = ?0 and containerId = ?1";

    public static final String FIND_BY_TAG = "select * from content where tags contains ?0";

    @Query(UPDATE_REPUTATION)
    Mono<Object> updateReputationById(UUID id, UUID containerId, Reputation reputation);

    @Override
    default Mono<Reputation> updateReputationById(MapId id, Reputation reputation) {
        return updateReputationById((UUID) id.get(IDENTITY), (UUID) id.get(CONTAINER_ID), reputation)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from content where containerid = ?0")
    Mono<Object> deleteAllByContainerId(UUID containerId);

    default Mono<Void> deleteContentWithChildren(MapId mapId) {
        return deleteById(mapId).then(RelationshipService.deleteChildren(mapId));
    }

    @Query(FIND_BY_TAG)
    Flux<Content> findAllByTag(String tag);

    @Query(FIND_BY_TAG)
    Mono<Slice<Content>> findAllByTag(String tag, Pageable pageable);

}
