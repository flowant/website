package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.Content;
import org.flowant.website.repository.BackendContentRepository;
import org.flowant.website.storage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ContentRest extends BaseRepositoryRest<Content, BackendContentRepository> {
    public final static String ID = "id";
    public final static String CONTENT = "/content";
    public final static String CONTENT__ID__ = "/content/{id}";

    @Autowired
    private BackendContentRepository contentRepository;

    @GetMapping(value = CONTENT)
    public Flux<Content> getAll() {
        return super.getAll();
    }

    @PostMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> post(@Valid @RequestBody Content content) {
        return super.post(content);
    }

    @PutMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> put(@Valid @RequestBody Content content) {
        return super.put(content);
    }

    @GetMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Content>> getById(@PathVariable(value = ID) String id) {
        return super.getById(id);
    }

    // If File Server is separated, we can use FILES_DELETES end point instead of FileStorage.deleteAll
    @DeleteMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return contentRepository.findById(UUID.fromString(id))
                .doOnNext(content-> FileStorage.deleteAll(content.getFileRefs()))
                .then(contentRepository.deleteById(UUID.fromString(id))
                .map(ResponseEntity::ok));
    }
}
