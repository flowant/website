package org.flowant.website.rest;

import java.util.UUID;
import java.util.function.BiFunction;

import javax.validation.Valid;

import org.flowant.website.model.Relation;
import org.flowant.website.repository.RelationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class RelationRest extends RepositoryRest<Relation, UUID, RelationRepository> {

    public final static String PATH_RELATION = "/relation";
    public final static String PATH_SEG_METHOD = "/{method}";
    public final static String PATH_SEG_FOLLOWEE = "/{followee}";
    public final static String METHOD = "method";
    public final static String FOLLOW = "follow";
    public final static String UNFOLLOW = "unfollow";
    public final static String FOLLOWEE = "followee";

    @PostMapping(value = PATH_RELATION)
    public Mono<ResponseEntity<Relation>> post(@Valid @RequestBody Relation relation) {
        return super.post(relation);
    }

    @PostMapping(value = PATH_RELATION + PATH_SEG_METHOD + PATH_SEG_ID + PATH_SEG_FOLLOWEE)
    public Mono<ResponseEntity<Relation>> put(
            @PathVariable(value = METHOD) String method,
            @PathVariable(value = ID) String id,
            @PathVariable(value = FOLLOWEE) String followee) {

        BiFunction<UUID, UUID, Mono<UUID>> m;

        switch (method.toLowerCase()) {
        case FOLLOW:
            m = repo::follow;
            break;
        case UNFOLLOW:
            m = repo::unfollow;
            break;
        default:
            return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        UUID follower = UUID.fromString(id);

        return m.apply(follower, UUID.fromString(followee))
                .then(super.getById(follower));
    }

    @PutMapping(value = PATH_RELATION)
    public Mono<ResponseEntity<Relation>> put(@Valid @RequestBody Relation relation) {
        return super.put(relation);
    }

    @GetMapping(value = PATH_RELATION + PATH_SEG_ID)
    public Mono<ResponseEntity<Relation>> getById(@PathVariable(value = ID) String id) {
        return super.getById(UUID.fromString(id));
    }

    @DeleteMapping(value = PATH_RELATION + PATH_SEG_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(UUID.fromString(id));
    }

}
