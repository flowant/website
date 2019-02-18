package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.ReplyReputation;
import org.flowant.website.repository.BackendReplyReputationRepository;
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
public class ReplyReputationRest extends
        ReputationRepositoryRest<ReplyReputation, BackendReplyReputationRepository> {

    public final static String REPLY_REPUTATION = "/reply_reputation";
    public final static String REPLY_REPUTATION__ID__ = REPLY_REPUTATION + PATH_SEG_ID;

    @GetMapping(value = REPLY_REPUTATION)
    public Flux<ReplyReputation> getAll() {
        return super.getAll();
    }

    @PostMapping(value = REPLY_REPUTATION)
    public Mono<ResponseEntity<ReplyReputation>> post(@Valid @RequestBody ReplyReputation user) {
        return super.post(user);
    }

    @PutMapping(value = REPLY_REPUTATION)
    public Mono<ResponseEntity<ReplyReputation>> put(@Valid @RequestBody ReplyReputation user) {
        return super.put(user);
    }

    @GetMapping(value = REPLY_REPUTATION__ID__)
    public Mono<ResponseEntity<ReplyReputation>> getById(@PathVariable(value = ID) String id) {
        return super.getById(id);
    }

    @DeleteMapping(value = REPLY_REPUTATION__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(id);
    }
}
