package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Message;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Mono;

public interface MessageRepository extends IdCidRepository<Message> {

    @Override
    @Query("delete from message where cid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

    Mono<Slice<Message>> findAllByAuthorId(UUID authorId, Pageable pageable);

}
