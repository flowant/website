package org.flowant.website.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;

import reactor.core.publisher.Mono;

public class ReputationFragmentImpl<T> implements ReputationFragment<T> {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<Reputation> updateReputationById(IdCid idCid, Reputation reputation, Class<T> entityClass) {

        return operations.update(
                Query.query(where("idCidIdentity").is(idCid.getIdentity()))
                        .and(where("idCidContainerId").is(idCid.getContainerId())),
                Update.empty().set("reputation", reputation),
                entityClass)
                .thenReturn(reputation);
    }

}
