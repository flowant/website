package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.User;
import org.flowant.website.repository.RelationshipService;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.storage.FileStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserRest extends RepositoryRest<User, UUID, UserRepository> {

    public final static String PATH_USER = "/user";

    @GetMapping(value = PATH_USER)
    public Flux<User> getAll() {
        return super.getAll();
    }

    @PostMapping(value = PATH_USER)
    public Mono<ResponseEntity<User>> post(@Valid @RequestBody User user) {
        return RelationshipService.createUserRelationship(user.getIdentity())
                .then(super.post(user));
    }

    @PutMapping(value = PATH_USER)
    public Mono<ResponseEntity<User>> put(@Valid @RequestBody User user) {
        return super.put(user);
    }

    @GetMapping(value = PATH_USER + PATH_SEG_ID)
    public Mono<ResponseEntity<User>> getById(@PathVariable(value = ID) String id) {
        return super.getById(UUID.fromString(id));
    }

    @DeleteMapping(value = PATH_USER + PATH_SEG_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        UUID userId = UUID.fromString(id);
        return repo.findById(userId)
                .doOnNext(u-> FileStorage.deleteAll(u.getFileRefs()))
                .then(repo.deleteByIdWithRelationship(userId)
                        .map(ResponseEntity::ok));
    }
}
