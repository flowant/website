package org.flowant.website.repository;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.IdScore;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.model.SubItem;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
public abstract class ReputationRepositoryTest <T extends ReputationCounter, R extends ReputationCounterRepository<T>>
        extends IdCidRepositoryTest<T, R> {

    @Autowired
    SubItemRepository repoSubItem;

    // Do not use this function in test case, It's a heavy IO bound function.
    @SuppressWarnings("unused")
    private void popularChildren(int cntPopular, int cntTotal, Function<IdCid, T> supplier, Class<?> reputationCls) {

        UUID containerId = IdMaker.randomUUID();

        Flux<T> reputations = Flux.range(1, cntTotal).map(i -> supplier.apply(IdCid.random(containerId))).cache();
        reputations.flatMap(r -> repo.save(r)).blockLast();
        cleaner.registerToBeDeleted(reputations);

        Collection<ReputationCounter> popular = RelationshipService.findPopularSubItems(cntPopular, containerId, reputationCls).block();

        List<T> expected = reputations.collectSortedList(Collections.reverseOrder(ReputationCounter::compare)).block();
        for (int i = 0; i < cntPopular; i++) {
            assertTrue(popular.contains(expected.get(i)));
        }

    }

    public void popularSubItems(int cntPopular, IdCid ContainerIdCid, BiFunction<IdCid, Reputation, T> supplier) {

        int cntTotal = cntPopular * 3;

        UUID containerId = ContainerIdCid.getIdentity();
        log.trace("popularSubItems, containerId:{}", containerId);

        Flux<T> reputations = Flux.range(1, cntTotal)
                .map(i -> supplier.apply(IdCid.random(containerId), ReputationMaker.randomReputationOverThreshold()))
                .cache();

        reputations.flatMap(r -> repo.save(r)).blockLast();
        cleaner.registerToBeDeleted(reputations);

        SubItem subItem = repoSubItem.findById(containerId).block();

        List<IdScore> idScores = subItem.getSubItems();

        List<T> expected = reputations.collectSortedList(Collections.reverseOrder(ReputationCounter::compare)).block();

        log.trace("actual, size:{}", idScores::size);
        idScores.forEach(log::trace);
        log.trace("expected, size:{}", expected::size);
        expected.forEach(log::trace);

        for (int i = 0; i < cntPopular; i++) {
            assertTrue(idScores.contains(expected.get(i).toIdScore()));
        }

    }

}
