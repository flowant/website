package org.flowant.website.repository;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.util.IdMaker;

import reactor.core.publisher.Flux;

public abstract class ReputationRepositoryTest <T extends ReputationCounter, R extends ReputationCounterRepository<T>>
        extends IdCidRepositoryTest<T, R> {

    public void popularChildren(int cntPopular, int cntTotal, Function<IdCid, T> supplier, Class<?> containerCls) {

        UUID containerId = IdMaker.randomUUID();

        Flux<T> reputations = Flux.range(1, cntTotal).map(i -> supplier.apply(IdCid.random(containerId))).cache();
        reputations.flatMap(r -> repo.save(r)).blockLast();
        cleaner.registerToBeDeleted(reputations);

        Collection<ReputationCounter> popular = RelationshipService.popularChildren(cntPopular, containerId, containerCls).block();

        List<T> expected = reputations.collectSortedList(Comparator.comparing(T::getLiked).reversed()).block();
        for (int i = 0; i < cntPopular; i++) {
            assertTrue(popular.contains(expected.get(i)));
        }

    }

}
