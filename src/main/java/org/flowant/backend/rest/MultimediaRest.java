package org.flowant.backend.rest;

import java.nio.file.Paths;

import org.flowant.backend.repository.MultimediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class MultimediaRest {
    final static String ID = "id";
    final static String CID = "cid";
    final static String FILE = "file";
    final static String MULTIMEDIA = "/multimedia";
    final static String MULTIMEDIA_STREAM = "/multimedia/stream";
    final static String MULTIMEDIA__ID__ = "/multimedia/{id}";

    @Autowired
    private MultimediaRepository multimediaRepository;

//    public Multimedia fromFilePart(Mono<FilePart> file) {
//        Multimedia mm = new Multimedia();
//        mm.setId(UUID.randomUUID());
//        mm.setCrudTime(CRUDZonedTime.now());
//        
//    }

    @PostMapping(value = MULTIMEDIA)
    public Mono<Void> post(@RequestPart("file") Mono<FilePart> file) {
        return file.flatMap(filePart -> {
            String filename = filePart.filename();
            log.debug("post save filename:{}", filename);
            return filePart.transferTo(Paths.get(filename));
        });
    }

    @PostMapping(value = MULTIMEDIA + "s") // TODO change name
    public Flux<Void> postAll(@RequestPart("file") Flux<FilePart> file) {
        return file.flatMap(filePart -> {
            String filename = filePart.filename();
            log.debug("post save filename:{}", filename);
            return filePart.transferTo(Paths.get(filename));
        });
    }

//    @PostMapping(value = MULTIMEDIA)
//    public Mono<ResponseEntity<Multimedia>> post(@Valid @RequestBody Multimedia multimedia) {
//        return multimediaRepository.save(multimedia).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PutMapping(value = MULTIMEDIA__ID__)
//    public Mono<ResponseEntity<Multimedia>> putById(@PathVariable(value = ID) String id, @Valid @RequestBody Multimedia multimedia) {
//        // TODO need update policy
//        return multimediaRepository.save(multimedia).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @GetMapping(value = MULTIMEDIA__ID__)
//    public Mono<ResponseEntity<Multimedia>> getById(@PathVariable(value = ID) String id) {
//        return multimediaRepository.findById(UUID.fromString(id)).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @DeleteMapping(value = MULTIMEDIA__ID__)
//    public Mono<ResponseEntity<Void>> delete(@PathVariable(value = ID) String id) {
//        return multimediaRepository.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
//    }
}
