package org.flowant.website.repository;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public abstract class IdCidRepositoryTest <T extends HasIdCid, R extends IdCidRepository<T>>
        extends RepositoryTest<T, IdCid, R> {

    public void testDeleteAllByContainerId(T entity, Function<T, UUID> getContainerId) {
        cleaner.registerToBeDeleted(entity);

        Mono<T> saveAndDeleteAndFind = repo.save(entity)
                .then(repo.deleteAllByIdCidContainerId(getContainerId.apply(entity)))
                .then(repo.findById(entity.getIdCid()));
        StepVerifier.create(saveAndDeleteAndFind).expectNextCount(0).verifyComplete();
    }

}

