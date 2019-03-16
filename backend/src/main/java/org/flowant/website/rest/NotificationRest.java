package org.flowant.website.rest;

import static org.flowant.website.repository.PageableUtil.pageable;
import static org.flowant.website.rest.LinkUtil.nextLinkHeader;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.WebSiteConfig;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.flowant.website.repository.NotificationRepository;
import org.flowant.website.repository.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@RestController
public class NotificationRest extends IdCidRepositoryRest<Notification, NotificationRepository> {

    public final static String PATH_NOTIFICATION = "/notification";
    public final static String SID = "sid";
    public final static String SUBSCRIBER = "subscriber";
    public final static String PATH_SEG_SUBSCRIBER = "/{subscriber}";

    @Autowired
    RelationRepository repoRelation;

    @Autowired
    WebSiteConfig config;

    @GetMapping(value = PATH_NOTIFICATION)
    public Mono<ResponseEntity<List<Notification>>> getAllByContainerId(
            @RequestParam(value = CID, required = false) String containerId,
            @RequestParam(value = SID, required = false) String subscriberId,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        UriComponentsBuilder uriBuilderWithPath = uriBuilder.path(PATH_NOTIFICATION);

        if (containerId != null) {
            return super.getAllByContainerId(containerId, page, size, pagingState, uriBuilderWithPath);

        } else if (subscriberId != null) {
            return repo.findAllBySubscriberId(UUID.fromString(subscriberId), pageable(page, size, pagingState))
                    .map(slice -> ResponseEntity.ok()
                            .headers(nextLinkHeader(SID, subscriberId, uriBuilderWithPath, slice))
                            .body(slice.getContent()))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping(value = PATH_NOTIFICATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Notification>> getById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @PostMapping(value = PATH_NOTIFICATION)
    public Mono<ResponseEntity<Notification>> post(@Valid @RequestBody Notification notification) {

        switch(notification.getCategory()) {
        case NC:
            return repoRelation.findById(notification.getContainerId())
                    .map(relation -> {
                        notification.getSubscribers().addAll(relation.getFollowers());
                        return notification;
                    }).flatMap(this::saveWithTtl);
        default:
            return saveWithTtl(notification);
        }
    }

    public Mono<ResponseEntity<Notification>> saveWithTtl(Notification notification) {

        return repo.saveWithTtl(notification, config.getTtlNotifications())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = PATH_NOTIFICATION)
    public Mono<ResponseEntity<Notification>> put(@Valid @RequestBody Notification notification) {

        return post(notification);
    }

    @DeleteMapping(value = PATH_NOTIFICATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }

    @DeleteMapping(value = PATH_NOTIFICATION + PATH_SEG_ID_CID + PATH_SEG_SUBSCRIBER)
    public Mono<ResponseEntity<UUID>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid,
            @PathVariable(value = SUBSCRIBER) String subscriber) {

        return repo.removeSubscriber(IdCid.of(id, cid), UUID.fromString(subscriber))
                .map(ResponseEntity::ok);
    }

}
