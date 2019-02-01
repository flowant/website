package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Content;
import org.flowant.website.repository.BackendContentRepository;
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
public class ContentRest {
    final static String ID = "id";
    final static String CONTENT = "/content";
    final static String CONTENT_STREAM = "/content/stream";
    final static String CONTENT__ID__ = "/content/{id}";

    @Autowired
    private BackendContentRepository contentRepository;

    @GetMapping(value = CONTENT)
    public Flux<Content> getAll() {
        return contentRepository.findAll().doOnNext(content -> content.getCruTime().readNow());
    }

    @GetMapping(value = CONTENT_STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Content> getAllStream() {
        return contentRepository.findAll().doOnNext(content -> content.getCruTime().readNow());
    }

    @PostMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> post(@Valid @RequestBody Content content) {
        content.setCruTime(CRUZonedTime.now());
        return contentRepository.save(content).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> put(@Valid @RequestBody Content content) {
        content.getCruTime().updatedNow();
        return contentRepository.save(content).map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Content>> getById(@PathVariable(value = ID) String id) {
        return contentRepository.findById(UUID.fromString(id)).doOnNext(content -> content.getCruTime().readNow())
                .map(ResponseEntity::ok).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Void>> delete(@PathVariable(value = ID) String id) {
        return contentRepository.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
    }
}
