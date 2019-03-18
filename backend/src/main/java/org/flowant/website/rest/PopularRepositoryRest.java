package org.flowant.website.rest;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class PopularRepositoryRest <T extends HasIdCid & HasReputation & HasCruTime, R extends ReputationRepository<T>>
        extends CruTimeRepositoryRest <T, R> {

    public final static String PATH_POPULAR = "/popular";

    @Autowired
    SubItemRepository repoSubItem;

    public Mono<ResponseEntity<List<T>>> getPopularSubItemByContainerId(String containerId) {

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
