package org.flowant.website.rest;

import static org.flowant.website.rest.LinkUtil.nextLinkHeader;
import static org.flowant.website.rest.LinkUtil.pageable;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.HasCruTime;
import org.flowant.website.repository.PageableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

public class PageableRepositoryRest <Entity extends HasCruTime, Repository extends PageableRepository<Entity, UUID>>
        extends BaseRepositoryRest <Entity, Repository> {

    public final static String ID = "id";
    public final static String PAGE = "page";
    public final static String SIZE = "size";
    public final static String CID = "cid"; // ContainerId
    public final static String PS = "ps"; // pagingState

    @Autowired
    protected Repository repo;

    public Mono<ResponseEntity<List<Entity>>> getAllByContainerId(@RequestParam(CID) String containerId,
            @RequestParam(PAGE) int page, @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return repo.findAllByContainerId(UUID.fromString(containerId), pageable(page, size, pagingState))
                .map(slice -> ResponseEntity.ok()
                        .headers(nextLinkHeader(containerId, uriBuilder, slice))
                        .body(slice.getContent()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
