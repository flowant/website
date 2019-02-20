package org.flowant.website.repository;

import static org.flowant.website.model.HasMapId.CONTAINER_ID;
import static org.flowant.website.model.HasMapId.IDENTITY;

import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.Reputation;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ContentRepository extends PageableRepository<Content, MapId> {

    static final String UPDATE_REPUTATION = "UPDATE content SET reputation = ?2 " +
            "WHERE identity = ?0 and containerId = ?1";

    @Query(UPDATE_REPUTATION)
    Mono<Object> updateReputationById(UUID id, UUID containerId, Reputation reputation);

    @Override
    default Mono<Reputation> updateReputationById(MapId id, Reputation reputation) {
        return updateReputationById((UUID) id.get(IDENTITY), (UUID) id.get(CONTAINER_ID), reputation)
                .thenReturn(reputation);
    }

}
