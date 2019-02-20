package org.flowant.website.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.HasMapId;
import org.flowant.website.model.HasReputation;
import org.junit.Assert;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import com.datastax.driver.core.utils.UUIDs;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
public abstract class PageableRepositoryTest <Entity extends HasMapId & HasReputation, Repository extends PageableRepository<Entity>>
        extends MapIdRepositoryTest<Entity, Repository> {

    UUID containerId;

    public Mono<Slice<Entity>> getAllPaging(int page, int size, @Nullable String pagingState) {
        Pageable pageable = PageableUtil.pageable(page, size, pagingState);
        return repo.findAllByContainerId(containerId, pageable);
    }

    public Mono<Slice<Entity>> getAllPaging(Pageable pageable) {
        log.trace("getAllPaging, pageable:{}", pageable);
        CassandraPageRequest cpr = CassandraPageRequest.class.cast(pageable);
        return getAllPaging(cpr.getPageNumber(), cpr.getPageSize(), cpr.getPagingState().toString());
    }

    public void findAllByContainerIdPageable(UUID containerId, Flux<Entity> entities) {
        this.containerId = containerId;
        entities = cleaner.registerToBeDeleted(entities);
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

    public void testOrdered(Function<Entity, MapId> getId, Comparator<Entity> comparator,
            Function<UUID, Entity> supplier) {

        UUID containerId = UUIDs.timeBased();
        // Entities's clusterKey is timeuuid. Entities are created by ascending order
        Flux<Entity> entities = Flux.range(1, 10).map(i -> supplier.apply(containerId)).cache();

        entities = cleaner.registerToBeDeleted(entities);
        repo.saveAll(entities).blockLast();

        // expected list is sorted by descending order
        List<Entity> sortedList = entities.collectSortedList(comparator).block();

        Flux<Entity> findAllByContainerId = repo.findAllByContainerId(containerId);
        StepVerifier.create(findAllByContainerId).expectNextSequence(sortedList).verifyComplete();
    }
}
