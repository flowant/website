package org.flowant.website.util.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DeleteAfterTest <Entity> {

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

    public void deleteRegistered(Consumer<Entity> deleter) {
        toBeDeletedEntities.forEach(deleter);
    }
}
