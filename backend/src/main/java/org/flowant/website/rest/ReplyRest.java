package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.Reply;
import org.flowant.website.repository.BackendReplyRepository;
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
public class ReplyRest extends BaseRepositoryRest<Reply, BackendReplyRepository> {
    public final static String ID = "id";
    public final static String REPLY = "/reply";
    public final static String REPLY__ID__ = REPLY + "/{id}";

    @GetMapping(value = REPLY)
    public Flux<Reply> getAll() {
        return super.getAll();
    }

    @PostMapping(value = REPLY)
    public Mono<ResponseEntity<Reply>> post(@Valid @RequestBody Reply reply) {
        return super.post(reply);
    }

    @PutMapping(value = REPLY)
    public Mono<ResponseEntity<Reply>> put(@Valid @RequestBody Reply reply) {
        return super.put(reply);
    }

    @GetMapping(value = REPLY__ID__)
    public Mono<ResponseEntity<Reply>> getById(@PathVariable(value = ID) String id) {
        return super.getById(id);
    }

    @DeleteMapping(value = REPLY__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(id);
    }

}
