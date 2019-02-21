package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.ReviewReputation;
import org.flowant.website.repository.BackendReviewReputationRepository;
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
public class ReviewReputationRest extends MapIdRepositoryRest<ReviewReputation, BackendReviewReputationRepository> {

    public final static String REVIEW_REPUTATION = "/review_reputation";

    @GetMapping(value = REVIEW_REPUTATION)
    public Flux<ReviewReputation> getAll() {
        return super.getAll();
    }

    @PostMapping(value = REVIEW_REPUTATION)
    public Mono<ResponseEntity<ReviewReputation>> post(@Valid @RequestBody ReviewReputation user) {
        return super.post(user);
    }

    @PutMapping(value = REVIEW_REPUTATION)
    public Mono<ResponseEntity<ReviewReputation>> put(@Valid @RequestBody ReviewReputation user) {
        return super.put(user);
    }

    @GetMapping(value = REVIEW_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<ReviewReputation>> getById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(toMapId(id, cid));
    }

    @DeleteMapping(value = REVIEW_REPUTATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(toMapId(id, cid));
    }
}
