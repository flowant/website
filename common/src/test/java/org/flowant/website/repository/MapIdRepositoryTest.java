package org.flowant.website.repository;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.HasMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class MapIdRepositoryTest <Entity extends HasMapId, Repository extends MapIdRepository<Entity>>
        extends RepositoryTest<Entity, MapId, Repository> {

    public void testDeleteAllByContainerId(Entity entity, Function<Entity, UUID> getContainerId) {
        cleaner.registerToBeDeleted(entity);

        Mono<Entity> saveAndDeleteAndFind = repo.save(entity)
                .then(repo.deleteAllByContainerId(getContainerId.apply(entity)))
                .then(repo.findById(entity.getMapId()));
        StepVerifier.create(saveAndDeleteAndFind).expectNextCount(0).verifyComplete();
    }
}

