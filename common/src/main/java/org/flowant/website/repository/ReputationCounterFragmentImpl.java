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

        //TODO Consider preparing the statement only once.

        final String cqlAccumulate = "UPDATE " + getTableName(operations, entityClass) +
              " SET v = v + ?, r = r + ?, l = l + ?, " +
              " d = d + ?, rt = rt + ?, rp = rp + ? " +
              " WHERE id = ? and cid = ?";

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
