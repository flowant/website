package org.flowant.website.rest;

import javax.validation.Valid;

import org.flowant.website.model.Review;
import org.flowant.website.repository.BackendReviewRepository;
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
public class ReviewRest extends BaseRepositoryRest<Review, BackendReviewRepository> {
    public final static String ID = "id";
    public final static String REVIEW = "/review";
    public final static String REVIEW__ID__ = REVIEW + "/{id}";

    @GetMapping(value = REVIEW)
    public Flux<Review> getAll() {
        return super.getAll();
    }

    @PostMapping(value = REVIEW)
    public Mono<ResponseEntity<Review>> post(@Valid @RequestBody Review review) {
        return super.post(review);
    }

    @PutMapping(value = REVIEW)
    public Mono<ResponseEntity<Review>> put(@Valid @RequestBody Review review) {
        return super.put(review);
    }

    @GetMapping(value = REVIEW__ID__)
    public Mono<ResponseEntity<Review>> getById(@PathVariable(value = ID) String id) {
        return super.getById(id);
    }

    @DeleteMapping(value = REVIEW__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(id);
    }

}
