package org.flowant.website.repository;

import static org.flowant.website.repository.CassandraTemplateUtil.getTableName;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;

import reactor.core.publisher.Mono;

public class ReputationFragmentImpl<T> implements ReputationFragment<T> {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation, Class<T> entityClass) {

        // TODO Consider preparing the statement only once.

        final String cqlUpdateReputation = "UPDATE " + getTableName(operations, entityClass) +
                " SET reputation = {viewed: ?, rated: ?, liked: ?, disliked: ?, reported: ?, reputed: ?} " +
                " WHERE identity = ? and containerId = ?";

        return operations.getReactiveCqlOperations().execute(cqlUpdateReputation,
                reputation.getViewed(),
                reputation.getRated(),
                reputation.getLiked(),
                reputation.getDisliked(),
                reputation.getReported(),
                reputation.getReputed(),
                idCid.getIdentity(),
                idCid.getContainerId())
                .thenReturn(reputation);
    }

}
