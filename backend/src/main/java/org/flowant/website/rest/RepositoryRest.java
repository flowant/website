package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.HasCruTime;
import org.flowant.website.model.HasMapId;
import org.flowant.website.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class RepositoryRest <Entity extends HasCruTime, ID, Repository extends IdentityRepository<Entity, ID>> {

    public final static String ID = "id";
    public final static String CID = "cid";
    public final static String PATH_SEG_ID_CID = "/{id}/{cid}";

    @Autowired
    protected Repository repo;

    public MapId toMapId(String identity, String containerId) {
        return BasicMapId.id().with(HasMapId.IDENTITY, UUID.fromString(identity))
                .with(HasMapId.CONTAINER_ID, UUID.fromString(containerId));
    }

    public Flux<Entity> getAll() {
        return repo.findAll().doOnNext(entity -> entity.getCruTime().readNow());
    }

    public Mono<ResponseEntity<Entity>> post(Entity entity) {
        entity.setCruTime(CRUZonedTime.now());
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> put(Entity entity) {
        entity.getCruTime().updatedNow();
        return repo.save(entity).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Entity>> getById(ID id) {
        return repo.findById(id).doOnNext(entity -> entity.getCruTime().readNow())
                .map(ResponseEntity::ok).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> delete(Entity entity) {
        return repo.delete(entity)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<Void>> deleteById(ID id) {
        return repo.deleteById(id)
                .map(ResponseEntity::ok);
    }
}
