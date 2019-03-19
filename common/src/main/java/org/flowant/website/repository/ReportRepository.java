package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Report;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import reactor.core.publisher.Mono;

public interface ReportRepository extends IdCidRepository<Report> {

    @Override
    @Query("delete from report where cid = ?0")
    Mono<Object> deleteAllByIdCidContainerId(UUID containerId);

    Mono<Slice<Report>> findAllByAuthorId(UUID authorId, Pageable pageable);

}
