package org.flowant.website.rest;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.flowant.website.model.Content;
import org.flowant.website.repository.BackendContentRepository;
import org.flowant.website.storage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.datastax.driver.core.PagingState;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class ContentRest extends BaseRepositoryRest<Content, BackendContentRepository> {
    public final static String ID = "id";
    public final static String CONTENT = "/content";
    public final static String CONTENT__ID__ = "/content/{id}";

    @Autowired
    private BackendContentRepository contentRepository;

    public Pageable pageable(int page, int size, @Nullable String pagingState) {
        log.trace("getAllPaging, page:{}, size:{}, pagingState:{}", page, size, pagingState);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pagingState != null) {
            pageRequest = CassandraPageRequest.of(pageRequest, PagingState.fromString(pagingState));
        }
        return pageRequest;
    }

    public String nextLink(String containerId, UriComponentsBuilder uriBuilder, Slice<Content> slice) {
        if (slice.hasNext() == false) {
            return "";
        }

        CassandraPageRequest pageable = CassandraPageRequest.class.cast(slice.nextPageable());

        String url = uriBuilder.path(CONTENT)
                .queryParam("cid", containerId)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .queryParam("ps", pageable.getPagingState())
                .build().encode().toUriString();

        return "<" + url + ">; rel=\"next\"";
    }

    @GetMapping(value = CONTENT)
    public Mono<ResponseEntity<List<Content>>> getAllByContainerId(@RequestParam("cid") String containerId,
            @RequestParam("page") int page, @RequestParam("size") int size,
            @RequestParam(value = "ps", required = false) String pagingState,
            UriComponentsBuilder uriBuilder) {

        return repo.findAllByContainerId(UUID.fromString(containerId), pageable(page, size, pagingState))
                .map(slice -> ResponseEntity.ok()
                        .header("Link", nextLink(containerId, uriBuilder, slice))
                        .body(slice.getContent()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> post(@Valid @RequestBody Content content) {
        return super.post(content);
    }

    @PutMapping(value = CONTENT)
    public Mono<ResponseEntity<Content>> put(@Valid @RequestBody Content content) {
        return super.put(content);
    }

    @GetMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Content>> getById(@PathVariable(value = ID) String id) {
        return super.getById(id);
    }

    // If File Server is separated, we can use FILES_DELETES end point instead of FileStorage.deleteAll
    @DeleteMapping(value = CONTENT__ID__)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable(value = ID) String id) {
        return contentRepository.findById(UUID.fromString(id))
                .doOnNext(content-> FileStorage.deleteAll(content.getFileRefs()))
                .then(contentRepository.deleteById(UUID.fromString(id))
                .map(ResponseEntity::ok));
    }
}
