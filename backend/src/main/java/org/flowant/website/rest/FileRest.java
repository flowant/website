package org.flowant.website.rest;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.FileRef;
import org.flowant.website.storage.FileStorage;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FileRest {
    public static final String ID = "id";
    public static final String CONTENTID = "contentId";
    public static final String ATTACHMENT = "attachment";
    public static final String PATH_FILES = "/files";
    public static final String PATH_FILES_SEG_ID = "/files/{id}";
    public static final String PATH_FILES_DELETES = "/files/deletes";

    @PostMapping(value = PATH_FILES)
    public Mono<ResponseEntity<List<FileRef>>> postAll(@RequestPart(ATTACHMENT) Flux<FilePart> files) {

        return FileStorage.saveAll(files)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = PATH_FILES_SEG_ID)
    public Mono<ResponseEntity<FileRef>> post(@PathVariable(value = ID) String id, @RequestPart(ATTACHMENT) FilePart file) {

        return FileStorage.save(UUID.fromString(id), file)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //TODO CACHE
    @GetMapping(value = PATH_FILES_SEG_ID)
    public Mono<ResponseEntity<Resource>> getById(@PathVariable(value = ID) String id) {

        return FileStorage.findById(id)
                .map(res -> ResponseEntity.ok()
                        .header(CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"")
                        .body(res))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = PATH_FILES_SEG_ID)
    public Mono<ResponseEntity<Boolean>> deleteById(@PathVariable(value = ID) String id) {

        return FileStorage.deleteById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = PATH_FILES_DELETES)
    public Mono<ResponseEntity<Boolean>> deleteAll(@RequestBody Flux<FileRef> files) {

        return FileStorage.deleteAll(files)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
