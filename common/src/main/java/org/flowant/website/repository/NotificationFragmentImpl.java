package org.flowant.website.repository;

import static org.springframework.data.cassandra.core.query.Criteria.where;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;

import reactor.core.publisher.Mono;

public class NotificationFragmentImpl implements NotificationFragment {

    @Autowired
    private ReactiveCassandraOperations operations;

    @Override
    public Mono<UUID> removeSubscriber(IdCid idCid, UUID subscriber) {
        return operations.update(
                Query.query(where("idCidIdentity").is(idCid.getIdentity()))
                        .and(where("idCidContainerId").is(idCid.getContainerId())),
                Update.empty().remove("subscribers", subscriber),
                Notification.class)
                .thenReturn(subscriber);
    }

}
