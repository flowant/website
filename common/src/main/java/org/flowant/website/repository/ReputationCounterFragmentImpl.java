package org.flowant.website.repository;

import static org.flowant.website.repository.CassandraTemplateUtil.getTableName;

import org.flowant.website.model.ReputationCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;

import reactor.core.publisher.Mono;

public class ReputationCounterFragmentImpl<T extends ReputationCounter> implements ReputationCounterFragment<T> {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<Boolean> accumulate(T entity, Class<T> entityClass) {

      String cqlAccumulate = "UPDATE " + getTableName(operations, entityClass) +
              " SET viewed = viewed + ?, rated = rated + ?, liked = liked + ?, " +
              " disliked = disliked + ?, reported = reported + ?, reputed = reputed + ? " +
              " WHERE identity = ? and containerid = ?";

      return operations.getReactiveCqlOperations().execute(cqlAccumulate,
              entity.getViewed(),
              entity.getRated(),
              entity.getLiked(),
              entity.getDisliked(),
              entity.getReported(),
              entity.getReputed(),
              entity.getIdentity(),
              entity.getContainerId());
    }

}
