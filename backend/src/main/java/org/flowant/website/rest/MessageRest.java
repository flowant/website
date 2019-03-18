package org.flowant.website.rest;

import java.util.List;

import javax.validation.Valid;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Message;
import org.flowant.website.repository.MessageRepository;
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
public class MessageRest extends IdCidRepositoryRest<Message, MessageRepository> {

    public final static String PATH_MESSAGE = "/message";

    @GetMapping(value = PATH_MESSAGE)
    public Mono<ResponseEntity<List<Message>>> getAllByParam(
            @RequestParam(value = CID, required = false) String containerId,
            @RequestParam(value = AID, required = false) String authorId,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        UriComponentsBuilder uriBuilderWithPath = uriBuilder.path(PATH_MESSAGE);

        if (containerId != null) {
            return super.getAllByParam(repo::findAllByIdCidContainerId, CID, containerId,
                    page, size, pagingState, uriBuilderWithPath);
        } else if (authorId != null) {
            return super.getAllByParam(repo::findAllByAuthorId, AID, authorId,
                    page, size, pagingState, uriBuilderWithPath);
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping(value = PATH_MESSAGE + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Message>> getById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @PostMapping(value = PATH_MESSAGE)
    public Mono<ResponseEntity<Message>> post(@Valid @RequestBody Message message) {

        return super.post(message);
    }

    @PutMapping(value = PATH_MESSAGE)
    public Mono<ResponseEntity<Message>> put(@Valid @RequestBody Message message) {

        return super.put(message);
    }

    @DeleteMapping(value = PATH_MESSAGE + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(IdCid.of(id, cid));
    }

}
