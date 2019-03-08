package org.flowant.website.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;
import org.junit.Assert;
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
public abstract class IdCidRepositoryTest <T extends HasIdCid, R extends IdCidRepository<T>>
        extends RepositoryTest<T, IdCid, R> {

    public void testDeleteAllByContainerId(T entity, Function<T, UUID> getContainerId) {
        cleaner.registerToBeDeleted(entity);

        Mono<T> saveAndDeleteAndFind = repo.save(entity)
                .then(repo.deleteAllByIdCidContainerId(getContainerId.apply(entity)))
                .then(repo.findById(entity.getIdCid()));
        StepVerifier.create(saveAndDeleteAndFind).expectNextCount(0).verifyComplete();
    }

    Function <Pageable, Mono<Slice<T>>> findPaging;

    private Mono<Slice<T>> getPaging(Pageable pageable) {
        log.trace("getAllPaging, pageable:{}", pageable);
        CassandraPageRequest cpr = CassandraPageRequest.class.cast(pageable);
        return getPaging(cpr.getPageNumber(), cpr.getPageSize(), cpr.getPagingState().toString());
    }

    private Mono<Slice<T>> getPaging(int page, int size, @Nullable String pagingState) {
        Pageable pageable = PageableUtil.pageable(page, size, pagingState);
        return findPaging.apply(pageable);
    }

    public void saveAndGetPaging(Flux<T> entities, Function <Pageable, Mono<Slice<T>>> findPaging) {
        this.findPaging = findPaging;
        entities = cleaner.registerToBeDeleted(entities);
        repo.saveAll(entities).blockLast();

        List<T> actual = new ArrayList<>();
        Slice<T> slice = getPaging(0, 3, null).block();
        while(true) {
            if (slice.hasContent()) {
                slice.forEach(log::trace);
                actual.addAll(slice.getContent());
            }
            if (!slice.hasNext()) {
                break;
            }
            slice = getPaging(slice.nextPageable()).block();
        }
        Assert.assertTrue(entities.all(actual::contains).block());
    }

    public void findAllByContainerIdPageable(UUID containerId, Flux<T> entities) {
        saveAndGetPaging(entities, pageable -> repo.findAllByIdCidContainerId(containerId, pageable));
    }

    public void testOrdered(Function<T, IdCid> getId, Comparator<T> comparator,
            Function<UUID, T> supplier) {

        UUID containerId = UUIDs.timeBased();
        // Entities's clusterKey is timeuuid. Entities are created by ascending order
        Flux<T> entities = Flux.range(1, 10).map(i -> supplier.apply(containerId)).cache();

        entities = cleaner.registerToBeDeleted(entities);
        repo.saveAll(entities).blockLast();

        // expected list is sorted by descending order
        List<T> sortedList = entities.collectSortedList(comparator).block();

        Flux<T> findAllByIdCidContainerId = repo.findAllByIdCidContainerId(containerId);
        StepVerifier.create(findAllByIdCidContainerId).expectNextSequence(sortedList).verifyComplete();
    }
}

