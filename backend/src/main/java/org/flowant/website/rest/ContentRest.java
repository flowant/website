package org.flowant.website.rest;

import java.util.List;

import javax.validation.Valid;

import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.storage.FileStorage;
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
public class ContentRest extends PopularRepositoryRest<Content, ContentRepository> {

    public final static String PATH_CONTENT = "/content";

    @GetMapping(value = PATH_CONTENT)
    public Mono<ResponseEntity<List<Content>>> getAllByParam(
            @RequestParam(value = CID, required = false) String containerId,
            @RequestParam(value = AID, required = false) String authorId,
            @RequestParam(PAGE) int page,
            @RequestParam(SIZE) int size,
            @RequestParam(value = PS, required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        UriComponentsBuilder uriBuilderWithPath = uriBuilder.path(PATH_CONTENT);

        if (containerId != null && authorId != null) {
            return super.getAllByParams(containerId, authorId, page, size, pagingState, uriBuilderWithPath);
        } else if (containerId != null) {
            return super.getAllByParam(repo::findAllByIdCidContainerId, CID, containerId,
                    page, size, pagingState, uriBuilderWithPath);
        } else if (authorId != null) {
            return super.getAllByParam(repo::findAllByAuthorId, AID, authorId,
                    page, size, pagingState, uriBuilderWithPath);
        } else {
            return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping(value = PATH_CONTENT + PATH_POPULAR)
    public Mono<ResponseEntity<List<Content>>> getPopularSubItemByContainerId(
            @RequestParam(value = CID) String containerId) {

        return super.getPopularSubItemByContainerId(containerId);
    }

    @GetMapping(value = PATH_CONTENT + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Content>> getById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        return super.getById(IdCid.of(id, cid));
    }

    @PostMapping(value = PATH_CONTENT)
    public Mono<ResponseEntity<Content>> post(@Valid @RequestBody Content content) {

        return super.post(content);
    }

    @PutMapping(value = PATH_CONTENT)
    public Mono<ResponseEntity<Content>> put(@Valid @RequestBody Content content) {

        return super.put(content);
    }

    // If File Server is separated, we can use FILES_DELETES end point instead of FileStorage.deleteAll
    @DeleteMapping(value = PATH_CONTENT + PATH_SEG_ID_CID)
    public Mono<ResponseEntity<Void>> deleteById(
            @PathVariable(value = ID) String id,
            @PathVariable(value = CID) String cid) {

        IdCid idCid = IdCid.of(id, cid);
        return repo.findById(idCid)
                .doOnNext(content-> FileStorage.deleteAll(content.getFileRefs()))
                .then(super.deleteById(idCid));
    }
}
