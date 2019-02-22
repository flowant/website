package org.flowant.website.repository;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class IdCidRepositoryTest <Entity extends HasIdCid, Repository extends IdCidRepository<Entity>>
        extends RepositoryTest<Entity, IdCid, Repository> {

    public void testDeleteAllByContainerId(Entity entity, Function<Entity, UUID> getContainerId) {
        cleaner.registerToBeDeleted(entity);

        Mono<Entity> saveAndDeleteAndFind = repo.save(entity)
                .then(repo.deleteAllByIdCidContainerId(getContainerId.apply(entity)))
                .then(repo.findById(entity.getIdCid()));
        StepVerifier.create(saveAndDeleteAndFind).expectNextCount(0).verifyComplete();
    }

}

