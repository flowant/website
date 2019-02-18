package org.flowant.website.rest;

import static org.flowant.website.repository.PageableUtil.pageable;
import static org.flowant.website.rest.LinkUtil.nextLinkHeader;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.HasCruTime;
import org.flowant.website.repository.PageableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

public abstract class PageableRepositoryRest <Entity extends HasCruTime, ID, Repository extends PageableRepository<Entity, ID>>
        extends RepositoryRest <Entity, ID, Repository> {

    public final static String PAGE = "page";
    public final static String SIZE = "size";
    public final static String CID = "cid"; // ContainerId
    public final static String PS = "ps"; // pagingState

    @Autowired
    protected Repository repo;

    public Mono<ResponseEntity<List<Entity>>> getAllByContainerId(String containerId,
            int page, int size, String pagingState, UriComponentsBuilder uriBuilder) {

        return repo.findAllByContainerId(UUID.fromString(containerId), pageable(page, size, pagingState))
                .map(slice -> ResponseEntity.ok()
                        .headers(nextLinkHeader(containerId, uriBuilder, slice))
                        .body(slice.getContent()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
