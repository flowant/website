package org.flowant.website.rest;

import java.util.List;

import javax.validation.Valid;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.flowant.website.repository.NotificationRepository;
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

    public final static String NOTIFICATION = "/notification";

    @GetMapping(value = NOTIFICATION)
    public Mono<ResponseEntity<List<Notification>>> getAllByContainerId(
            @RequestParam(CID) String containerId,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return super.getAllByContainerId(containerId, page, size, pagingState, uriBuilder.path(NOTIFICATION));
    }

    @GetMapping(value = NOTIFICATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Notification>> getById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @PostMapping(value = NOTIFICATION)
    public Mono<ResponseEntity<Notification>> post(@Valid @RequestBody Notification notification) {

        return super.post(notification);
    }

    @PutMapping(value = NOTIFICATION)
    public Mono<ResponseEntity<Notification>> put(@Valid @RequestBody Notification notification) {

        return super.put(notification);
    }

    @DeleteMapping(value = NOTIFICATION + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }

}
