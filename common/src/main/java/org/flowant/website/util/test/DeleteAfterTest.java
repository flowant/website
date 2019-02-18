package org.flowant.website.util.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public abstract class DeleteAfterTest <Entity, ID, Repository extends ReactiveCrudRepository<Entity, ID>> {
    @Autowired
    protected Repository repo;

    protected List<Entity> toBeDeletedEntities = new ArrayList<Entity>();

    public void registerToBeDeleted(Entity entity) {
        toBeDeletedEntities.add(entity);
    }

    public void registerToBeDeleted(List<Entity> entities) {
        toBeDeletedEntities.addAll(entities);
    }

    public Mono<Entity> registerToBeDeleted(Mono<Entity> mono) {
        Mono<Entity> cached = mono.cache();
        toBeDeletedEntities.add(cached.block());
        return cached;
    }

    public Flux<Entity> registerToBeDeleted(Flux<Entity> flux) {
        Flux<Entity> cached = flux.cache();
        toBeDeletedEntities.addAll(cached.collectList().block());
        return cached;
    }

    public void deleteRegistered() {
        toBeDeletedEntities.forEach(entity -> {
            log.trace("delete entity:{}", entity);
            repo.delete(entity).subscribe();
        });
    }
}
