package org.flowant.website.repository;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.flowant.website.model.HasReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.util.test.DeleteAfterTest;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class RepositoryTest <T, ID, R extends ReactiveCassandraRepository<T, ID>> {

    @Autowired
    R repo;

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    protected DeleteAfterTest<T> cleaner = new DeleteAfterTest<>();

    Consumer<T> deleter = entity -> repo.delete(entity).subscribe();

    public void setDeleter(Consumer<T> deleter) {
        this.deleter = deleter;
    }

    @After
    public void after() {
        cleaner.deleteRegistered(deleter);
    }

    public void save(T entity, Function<T, ID> getId) {
        cleaner.registerToBeDeleted(entity);

        Flux<T> saveThenFind = repo.save(entity).thenMany(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(entity).verifyComplete();
    }

    public void saveAllFindAllById(Supplier<T> supplier, Function<T, ID> getId) {
        Flux<T> entities = Flux.range(1, 5).map(i -> supplier.get());
        entities = cleaner.registerToBeDeleted(entities);

        Flux<T> saveAllThenFind = repo.saveAll(entities)
                .thenMany(repo.findAllById(entities.map(getId)));
        StepVerifier.create(saveAllThenFind).expectNextCount(5).verifyComplete();
    }

    public void testCrud(Function<T, ID> getId, Supplier<T> small, Supplier<T> large) {
        save(small.get(), getId);
        save(large.get(), getId);
        saveAllFindAllById(large, getId);
    }

    public void testAccumulation(Function<T, ID> getId, BiFunction<IdCid, Reputation, T> supplier) {
        save(ReputationMaker.emptyTypeReputation(supplier), getId);
        save(ReputationMaker.randomTypeReputation(supplier), getId);
        accumulateCounter(getId, supplier);
    }

    public void accumulateCounter(Function<T, ID> getId, BiFunction<IdCid, Reputation, T> supplier) {
        IdCid idCid = IdCid.random();

        T entity = ReputationMaker.emptyTypeReputation(idCid, supplier);
        cleaner.registerToBeDeleted(entity);

        T acc = ReputationMaker.randomTypeReputation(idCid, supplier);
        cleaner.registerToBeDeleted(acc);

        Mono<T> saveThenFind = repo.save(entity).then(repo.save(acc))
                .then(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();

        Mono<? extends HasReputation> findReputationFromUpdatedParent =
                RelationshipService.findReputation((ReputationCounter) entity);

        StepVerifier.create(findReputationFromUpdatedParent)
                .consumeNextWith(hasReputation -> Assert.assertEquals(acc,
                        supplier.apply(idCid, hasReputation.getReputation())))
                .verifyComplete();
    }

}

