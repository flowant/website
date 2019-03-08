package org.flowant.website.rest;

import java.util.List;

import javax.validation.Valid;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Message;
import org.flowant.website.repository.MessageRepository;
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
public class MessageRest extends IdCidRepositoryRest<Message, MessageRepository> {

    public final static String MESSAGE = "/message";

    @GetMapping(value = MESSAGE)
    public Mono<ResponseEntity<List<Message>>> getAllByContainerId(
            @RequestParam(CID) String containerId,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return super.getAllByContainerId(containerId, page, size, pagingState, uriBuilder.path(MESSAGE));
    }

    @GetMapping(value = MESSAGE + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Message>> getById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @PostMapping(value = MESSAGE)
    public Mono<ResponseEntity<Message>> post(@Valid @RequestBody Message message) {

        return super.post(message);
    }

    @PutMapping(value = MESSAGE)
    public Mono<ResponseEntity<Message>> put(@Valid @RequestBody Message message) {

        return super.put(message);
    }

    @DeleteMapping(value = MESSAGE + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }

}
