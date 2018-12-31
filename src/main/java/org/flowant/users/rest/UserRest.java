package org.flowant.users.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.users.data.User;
import org.flowant.users.data.UserRepository;
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

import com.datastax.driver.core.utils.UUIDs;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserRest {
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping(value = "/user")
    public Flux<User> getAllUser() {
        return userRepository.findAll();
    }
    
    @GetMapping(value = "/user/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamAllUser() {
        return userRepository.findAll();
    }
    
    @PostMapping(value = "/user")
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody User userInput) {
        userInput.setId(UUIDs.timeBased());
        return userRepository.save(userInput).map(user -> {
            return ResponseEntity.ok(user);
        }).defaultIfEmpty(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping(value = "/user/{id}")
    public Mono<ResponseEntity<User>> getPersonById(@PathVariable(value = "id") String id) {
        return userRepository.findById(UUID.fromString(id)).map(person -> {
            return ResponseEntity.ok(person);
        }).defaultIfEmpty(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PutMapping(value = "/user/{id}")
    public Mono<ResponseEntity<User>> updatePerson(@PathVariable(value = "id") String id, @Valid @RequestBody User userInput) {
        return userRepository.findById(UUID.fromString(id)).flatMap(user -> {
            user.setUsername(userInput.getUsername());
            return userRepository.save(user);
        }).map(updatedPerson -> {
            return ResponseEntity.ok(updatedPerson);
        }).defaultIfEmpty(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @DeleteMapping(value = "/user/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(value = "id") String id) {
        return userRepository.findById(UUID.fromString(id)).flatMap(person ->
            userRepository.delete(person).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
        ).defaultIfEmpty(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }
}
