package org.flowant.website.rest;

import static org.flowant.website.repository.PageableUtil.pageable;
import static org.flowant.website.rest.LinkUtil.nextLinkHeader;

import java.util.List;

import org.flowant.website.model.Content;
import org.flowant.website.repository.ContentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@RestController
public class SearchRest extends PageableRepositoryRest<Content, ContentRepository> {

    public final static String SEARCH = "/search";
    public final static String TAG = "tag";

    @GetMapping(value = SEARCH)
    public Mono<ResponseEntity<List<Content>>> findAllByTag(
            @RequestParam(TAG)String tag,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return repo.findAllByTag(tag, pageable(page, size, pagingState))
              .map(slice -> ResponseEntity.ok()
              .headers(nextLinkHeader(TAG, tag, uriBuilder.path(SEARCH), slice))
              .body(slice.getContent()))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
