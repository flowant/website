package org.flowant.website.repository;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.flowant.website.model.HasReputation;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.util.test.DeleteAfterTest;
import org.flowant.website.util.test.IdMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class RepositoryTest <Entity, ID, Repository extends ReactiveCrudRepository<Entity, ID>> {

    @Autowired
    Repository repo;

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    protected DeleteAfterTest<Entity> cleaner = new DeleteAfterTest<>();

    Consumer<Entity> deleter = entity -> repo.delete(entity).subscribe();

    public void setDeleter(Consumer<Entity> deleter) {
        this.deleter = deleter;
    }

    @After
    public void after() {
        cleaner.deleteRegistered(deleter);
    }

    public void save(Entity entity, Function<Entity, ID> getId) {
        cleaner.registerToBeDeleted(entity);

        Flux<Entity> saveThenFind = repo.save(entity).thenMany(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(entity).verifyComplete();
    }

    public void saveAllFindAllById(Supplier<Entity> supplier, Function<Entity, ID> getId) {
        Flux<Entity> entities = Flux.range(1, 5).map(i -> supplier.get());
        entities = cleaner.registerToBeDeleted(entities);

        Flux<Entity> saveAllThenFind = repo.saveAll(entities)
                .thenMany(repo.findAllById(entities.map(getId)));
        StepVerifier.create(saveAllThenFind).expectNextCount(5).verifyComplete();
    }

    public void testCrud(Function<Entity, ID> getId, Supplier<Entity> small, Supplier<Entity> large) {
        save(small.get(), getId);
        save(large.get(), getId);
        saveAllFindAllById(large, getId);
    }

    public void testAccumulation(Function<Entity, ID> getId, BiFunction<MapId, Reputation, Entity> supplier) {
        save(ReputationMaker.emptyTypeReputation(supplier), getId);
        save(ReputationMaker.randomTypeReputation(supplier), getId);
        accumulateCounter(getId, supplier);
    }

    public void accumulateCounter(Function<Entity, ID> getId, BiFunction<MapId, Reputation, Entity> supplier) {
        MapId mapId = IdMaker.randomMapId();

        Entity entity = ReputationMaker.emptyTypeReputation(mapId, supplier);
        cleaner.registerToBeDeleted(entity);

        Entity acc = ReputationMaker.randomTypeReputation(mapId, supplier);
        cleaner.registerToBeDeleted(acc);

        Mono<Entity> saveThenFind = repo.save(entity).then(repo.save(acc))
                .then(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(acc).verifyComplete();

        Mono<? extends HasReputation> findReputationFromUpdatedParent =
                RelationshipService.findReputation((ReputationCounter) entity);

        StepVerifier.create(findReputationFromUpdatedParent).consumeNextWith(hasReputation ->
                Assert.assertEquals(acc, supplier.apply(mapId, hasReputation.getReputation())))
                .verifyComplete();
    }

}

