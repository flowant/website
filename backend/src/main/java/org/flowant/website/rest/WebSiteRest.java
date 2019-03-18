package org.flowant.website.rest;

import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.WebSite;
import org.flowant.website.repository.WebSiteRepository;
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
public class WebSiteRest extends RepositoryRest<WebSite, UUID, WebSiteRepository> {

    public final static String PATH_WEBSITE = "/website";

    @GetMapping(value = PATH_WEBSITE)
    public Flux<WebSite> getAll() {
        return super.getAll();
    }

    @PostMapping(value = PATH_WEBSITE)
    public Mono<ResponseEntity<WebSite>> post(@Valid @RequestBody WebSite webSite) {
        return super.post(webSite);
    }

    @PutMapping(value = PATH_WEBSITE)
    public Mono<ResponseEntity<WebSite>> put(@Valid @RequestBody WebSite webSite) {
        return super.put(webSite);
    }

    @GetMapping(value = PATH_WEBSITE + PATH_SEG_ID)
    public Mono<ResponseEntity<WebSite>> getById(@PathVariable(value = ID) String id) {
        return super.getById(UUID.fromString(id));
    }

    @DeleteMapping(value = PATH_WEBSITE + PATH_SEG_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return super.deleteById(UUID.fromString(id));
    }

}
