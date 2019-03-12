package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Notification;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Mono;

public interface NotificationRepository extends NotificationFragment, IdCidRepository<Notification> {

    public static final String FIND_BY_SUBSCRIBER = "select * from notification where s contains ?0";

    @Query(FIND_BY_SUBSCRIBER)
    Mono<Slice<Notification>> findAllBySubscriberId(UUID identity, Pageable pageable);

    @Override
    @Query("delete from notification where cid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

}
