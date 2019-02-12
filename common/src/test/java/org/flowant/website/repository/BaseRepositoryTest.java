package org.flowant.website.repository;

import java.util.function.Function;
import java.util.function.Supplier;

import org.flowant.website.util.test.DeleteAfterTest;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BaseRepositoryTest <Entity, ID, Repository extends ReactiveCrudRepository<Entity, ID>>
        extends DeleteAfterTest <Entity, ID, Repository> {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @After
    public void after() {
        this.deleteRegistered();
    }

    public void save(Entity entity, Function<Entity, ID> getId) {
        registerToBeDeleted(entity);

        Flux<Entity> saveThenFind = repo.save(entity).thenMany(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(entity).verifyComplete();
    }

    public void saveAllFindAllById(Supplier<Entity> supplier, Function<Entity, ID> getId) {
        Flux<Entity> entities = Flux.range(1, 5).map(i -> supplier.get());
        entities = registerToBeDeleted(entities);

        Flux<Entity> saveAllThenFind = repo.saveAll(entities)
                .thenMany(repo.findAllById(entities.map(getId)));
        StepVerifier.create(saveAllThenFind).expectNextCount(5).verifyComplete();
    }

    public void testCrud(Function<Entity, ID> getId, Supplier<Entity> small, Supplier<Entity> large) {
        save(small.get(), getId);
        save(large.get(), getId);
        saveAllFindAllById(large, getId);
    }

}
