package org.flowant.backend.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.backend.model.User;
import org.flowant.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class UserRest {
    final static String ID = "id";
    final static String USER = "/user";
    final static String USER_STREAM = "/user/stream";
    final static String USER__ID__ = "/user/{id}";

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = USER)
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping(value = USER_STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getAllStream() {
        return userRepository.findAll();
    }

    @PostMapping(value = USER)
    public Mono<ResponseEntity<User>> post(@Valid @RequestBody User user) {
        return userRepository.save(user).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = USER__ID__)
    public Mono<ResponseEntity<User>> getById(@PathVariable(value = ID) String id) {
        return userRepository.findById(UUID.fromString(id)).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = USER__ID__)
    public Mono<ResponseEntity<User>> putById(@PathVariable(value = ID) String id, @Valid @RequestBody User user) {
        // TODO need update policy
        return userRepository.save(user).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = USER__ID__)
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(value = ID) String id) {
        return userRepository.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
    }
}
