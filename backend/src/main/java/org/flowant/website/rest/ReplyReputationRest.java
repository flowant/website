package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.repository.ReplyReputationRepository;
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
public class ReplyReputationRest extends IdCidRepositoryRest<ReplyReputation, ReplyReputationRepository> {

    public final static String PATH_REPLY_REPUTATION = "/reply_rpt";

    @GetMapping(value = PATH_REPLY_REPUTATION)
    public Flux<ReplyReputation> getAll() {
        return super.getAll();
    }

    @PostMapping(value = PATH_REPLY_REPUTATION)
    public Mono<ResponseEntity<ReplyReputation>> post(@Valid @RequestBody ReplyReputation user) {
        return super.post(user);
    }

    @PutMapping(value = PATH_REPLY_REPUTATION)
    public Mono<ResponseEntity<ReplyReputation>> put(@Valid @RequestBody ReplyReputation user) {
        return super.put(user);
    }

    @GetMapping(value = PATH_REPLY_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<ReplyReputation>> getById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @DeleteMapping(value = PATH_REPLY_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }
}
