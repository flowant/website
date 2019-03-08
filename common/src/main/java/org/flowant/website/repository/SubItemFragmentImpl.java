package org.flowant.website.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import java.util.UUID;

import org.flowant.website.model.IdScore;
import org.flowant.website.model.SubItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;

import reactor.core.publisher.Mono;

public class SubItemFragmentImpl implements SubItemFragment {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono <IdScore> addSubItem(UUID identity, IdScore idScore) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().addTo("subItems").append(idScore),
                SubItem.class)
                .thenReturn(idScore);
    }

    @Override
    public Mono<IdScore> removeSubItem(UUID identity, IdScore idScore) {

        return operations.update(
                Query.query(where("identity").is(identity)),
                Update.empty().remove("subItems", idScore),
                SubItem.class)
                .thenReturn(idScore);
    }

}
