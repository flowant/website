package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentReputationRepository;
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
public class ContentReputationRest extends IdCidRepositoryRest<ContentReputation, ContentReputationRepository> {

    public final static String CONTENT_REPUTATION = "/content_rpt";

    @GetMapping(value = CONTENT_REPUTATION)
    public Flux<ContentReputation> getAll() {
        return super.getAll();
    }

    @PostMapping(value = CONTENT_REPUTATION)
    public Mono<ResponseEntity<ContentReputation>> post(@Valid @RequestBody ContentReputation user) {
        return super.post(user);
    }

    @PutMapping(value = CONTENT_REPUTATION)
    public Mono<ResponseEntity<ContentReputation>> put(@Valid @RequestBody ContentReputation user) {
        return super.put(user);
    }

    @GetMapping(value = CONTENT_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<ContentReputation>> getById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @DeleteMapping(value = CONTENT_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }
}
