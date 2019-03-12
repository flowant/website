package org.flowant.website.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import org.flowant.website.model.ReputationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;

import reactor.core.publisher.Mono;

public class ReputationCounterFragmentImpl<T extends ReputationCounter> implements ReputationCounterFragment<T> {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<Boolean> accumulate(T entity) {

        return operations.update(
                Query.query(where("idCidIdentity").is(entity.getIdentity()))
                        .and(where("idCidContainerId").is(entity.getContainerId())),
                Update.empty().increment("viewed", entity.getViewed())
                        .increment("rated", entity.getRated())
                        .increment("liked", entity.getLiked())
                        .increment("disliked", entity.getDisliked())
                        .increment("reported", entity.getReported())
                        .increment("reputed", entity.getReputed()),
                entity.getClass());
    }

}
