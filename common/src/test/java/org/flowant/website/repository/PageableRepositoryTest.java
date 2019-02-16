package org.flowant.website.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import com.datastax.driver.core.PagingState;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public class PageableRepositoryTest <Entity, ID, Repository extends PageableRepository<Entity, ID>>
        extends BaseRepositoryTest<Entity, ID, Repository> {

    UUID containerId;

    public Mono<Slice<Entity>> getAllPaging(int page, int size, @Nullable String pagingState) {
        log.trace("getAllPaging, page:{}, size:{}, pagingState:{}", page, size, pagingState);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pagingState != null) {
            pageRequest = CassandraPageRequest.of(pageRequest, PagingState.fromString(pagingState));
        }
        log.trace("getAllPaging, pageRequest:{}", pageRequest);
        return repo.findAllByContainerId(containerId, pageRequest);
    }

    public Mono<Slice<Entity>> getAllPaging(Pageable pageable) {
        log.trace("getAllPaging, pageable:{}", pageable);
        CassandraPageRequest cpr = CassandraPageRequest.class.cast(pageable);
        return getAllPaging(cpr.getPageNumber(), cpr.getPageSize(), cpr.getPagingState().toString());
    }

    public void findAllByContainerIdPageable(UUID containerId, Flux<Entity> entities) {
        this.containerId = containerId;
        entities = registerToBeDeleted(entities);
        repo.saveAll(entities).blockLast();

        List<Entity> actual = new ArrayList<>();
        Slice<Entity> slice = getAllPaging(0, 3, null).block();
        while(true) {
            if (slice.hasContent()) {
                slice.forEach(log::trace);
                actual.addAll(slice.getContent());
            }
            if (!slice.hasNext()) {
                break;
            }
            slice = getAllPaging(slice.nextPageable()).block();
        }
        Assert.assertTrue(entities.all(actual::contains).block());
    }
}
