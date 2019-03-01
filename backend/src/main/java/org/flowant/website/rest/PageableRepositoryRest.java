package org.flowant.website.rest;

import static org.flowant.website.repository.PageableUtil.pageable;
import static org.flowant.website.rest.LinkUtil.nextLinkHeader;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.HasCruTime;
import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.HasReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.SubItem;
import org.flowant.website.repository.ReputationRepository;
import org.flowant.website.repository.SubItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class PageableRepositoryRest <Entity extends HasIdCid & HasReputation & HasCruTime, Repository extends ReputationRepository<Entity>>
        extends CruTimeRepositoryRest <Entity, Repository> {

    public final static String PAGE = "page";
    public final static String SIZE = "size";
    public final static String CID = "cid"; // containerId
    public final static String PS = "ps"; // pagingState
    public final static String POPULAR = "/popular";

    @Autowired
    SubItemRepository repoSubItem;

    public Mono<ResponseEntity<List<Entity>>> getAllByContainerId(String containerId,
            int page, int size, String pagingState, UriComponentsBuilder uriBuilder) {

        return repo.findAllByIdCidContainerId(UUID.fromString(containerId), pageable(page, size, pagingState))
                .map(slice -> ResponseEntity.ok()
                        .headers(nextLinkHeader(CID, containerId, uriBuilder, slice))
                        .body(slice.getContent()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<List<Entity>>> getPopularSubItemByContainerId(String containerId) {

        Flux<IdCid> idCids = repoSubItem.findById(UUID.fromString(containerId))
                .flatMapMany(SubItem::toIdCids);

        return repo.findAllById(idCids)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteById(IdCid idCid) {
        return repo.deleteByIdWithRelationship(idCid)
                .map(ResponseEntity::ok);
    }

}
