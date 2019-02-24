package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.model.Reputation;
import org.springframework.data.cassandra.repository.Query;

import reactor.core.publisher.Mono;

public interface ReplyRepository extends ReputationRepository<Reply>, ReputationFragment<Reply> {

    @Override
    default Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation) {
        return updateReputationById(idCid, reputation, Reply.class)
                .thenReturn(reputation);
    }

    @Override
    @Query("delete from reply where containerid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
