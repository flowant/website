package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.User;
import org.flowant.website.repository.UserRepository;
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
    public final static String ID = "id";
    public final static String USER = "/user";
    public final static String USER__ID__ = "/user/{id}";

    @GetMapping(value = USER)
    public Flux<User> getAll() {
        return super.getAll();
    }

    @PostMapping(value = USER)
    public Mono<ResponseEntity<User>> post(@Valid @RequestBody User user) {
        return super.post(user);
    }

    @PutMapping(value = USER)
    public Mono<ResponseEntity<User>> put(@Valid @RequestBody User user) {
        return super.put(user);
    }

    @GetMapping(value = USER__ID__)
    public Mono<ResponseEntity<User>> getById(@PathVariable(value = ID) String id) {
        return super.getById(UUID.fromString(id));
    }

    @DeleteMapping(value = USER__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(UUID.fromString(id));
    }
}
