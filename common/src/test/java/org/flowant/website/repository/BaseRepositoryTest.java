package org.flowant.website.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
public class BaseRepositoryTest <Entity, ID, Repository extends ReactiveCrudRepository<Entity, ID>> {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    Repository repo;

    protected List<Entity> toBeDeletedEntities = new ArrayList<Entity>();

    public void deleteAfterTest(Entity entity) {
        toBeDeletedEntities.add(entity);
    }

    public void deleteAfterTest(List<Entity> entities) {
        toBeDeletedEntities.addAll(entities);
    }

    public Mono<Entity> deleteAfterTest(Mono<Entity> mono) {
        Mono<Entity> cached = mono.cache();
        toBeDeletedEntities.add(cached.block());
        return cached;
    }

    public Flux<Entity> deleteAfterTest(Flux<Entity> flux) {
        Flux<Entity> cached = flux.cache();
        toBeDeletedEntities.addAll(cached.collectList().block());
        return cached;
    }

    @After
    public void after() {
        toBeDeletedEntities.forEach(entity -> {
            log.trace("delete entity:{}", entity);
            repo.delete(entity).subscribe();
        });
    }

    public void save(Entity entity, Function<Entity, ID> getId) {
        deleteAfterTest(entity);

        Flux<Entity> saveThenFind = repo.save(entity).thenMany(repo.findById(getId.apply(entity)));
        StepVerifier.create(saveThenFind).expectNext(entity).verifyComplete();
    }

    public void saveAllFindAllById(Supplier<Entity> supplier, Function<Entity, ID> getId) {
        Flux<Entity> entities = Flux.range(1, 5).map(i -> supplier.get());
        entities = deleteAfterTest(entities);

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
