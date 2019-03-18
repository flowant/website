package org.flowant.website.rest;

import static org.flowant.website.repository.PageableUtil.pageable;
import static org.flowant.website.rest.LinkUtil.nextLinkHeader;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.IdCidRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

public abstract class IdCidRepositoryRest <T extends HasIdCid, R extends IdCidRepository<T>>
        extends RepositoryRest<T, IdCid, R> {

    public final static String PAGE = "page";
    public final static String SIZE = "size";
    public final static String CID = "cid"; // containerId
    public final static String AID = "aid"; // authorId
    public final static String SID = "sid"; // subscriberId
    public final static String PS = "ps"; // pagingState
    public final static String PATH_SEG_ID_CID = "/{id}/{cid}";

    public Mono<ResponseEntity<List<T>>> getAllByContainerId(String containerId,
            int page, int size, String pagingState, UriComponentsBuilder uriBuilder) {

        return getAllByParam(repo::findAllByIdCidContainerId, CID, containerId, page, size, pagingState, uriBuilder);
    }

    public Mono<ResponseEntity<List<T>>> getAllByParam(BiFunction<UUID, Pageable, Mono<Slice<T>>> findAll,
            String paramName, String paramValue, int page, int size, String pagingState, UriComponentsBuilder uriBuilder) {

        return findAll.apply(UUID.fromString(paramValue), pageable(page, size, pagingState))
                .map(slice -> ResponseEntity.ok()
                        .headers(nextLinkHeader(paramName, paramValue, uriBuilder, slice))
                        .body(slice.getContent()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
