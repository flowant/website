package org.flowant.website.rest;

import java.util.List;

import javax.validation.Valid;

import org.flowant.website.model.Reply;
import org.flowant.website.repository.BackendReplyRepository;
import org.springframework.data.cassandra.core.mapping.MapId;
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
public class ReplyRest extends PageableRepositoryRest<Reply, MapId, BackendReplyRepository> {

    public final static String REPLY = "/reply";
    public final static String REPLY_ID_CID = REPLY + PATH_SEG_ID_CID;

    @GetMapping(value = REPLY)
    public Mono<ResponseEntity<List<Reply>>> getAllByContainerId(@RequestParam(CID) String containerId,
            @RequestParam(PAGE) int page, @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return super.getAllByContainerId(containerId, page, size, pagingState, uriBuilder.path(REPLY));
    }

    @PostMapping(value = REPLY)
    public Mono<ResponseEntity<Reply>> post(@Valid @RequestBody Reply reply) {
        return super.post(reply);
    }

    @PutMapping(value = REPLY)
    public Mono<ResponseEntity<Reply>> put(@Valid @RequestBody Reply reply) {
        return super.put(reply);
    }

    @GetMapping(value = REPLY_ID_CID)
    public Mono<ResponseEntity<Reply>> getById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(toMapId(id, cid));
    }

    @DeleteMapping(value = REPLY_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.deleteById(toMapId(id, cid));
    }

}
