package org.flowant.backend.rest;

import java.nio.file.Paths;

import org.flowant.backend.repository.FileRepository;
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
public class FileRest {
    final static String ID = "id";
    final static String CID = "cid";
    final static String ATTACHMENT = "attachment";
    final static String FILES = "/files";
    final static String FILE = "/file";
    final static String FILE_STREAM = "/file/stream";
    final static String FILE__ID__ = "/file/{id}";

    @Autowired
    private FileRepository fileRepository;

//    public File fromFilePart(Mono<FilePart> file) {
//        File mm = new File();
//        mm.setId(UUID.randomUUID());
//        mm.setCrudTime(CRUDZonedTime.now());
//        
//    }

    @PostMapping(value = FILE)
    public Mono<Void> post(@RequestPart(ATTACHMENT) Mono<FilePart> file) {
        return file.flatMap(filePart -> {
            String filename = filePart.filename();
            log.debug("post save filename:{}", filename);
            return filePart.transferTo(Paths.get(filename));
        });
    }

    @PostMapping(value = FILES) // TODO change name
    public Flux<Void> postAll(@RequestPart(ATTACHMENT) Flux<FilePart> file) {
        return file.flatMap(filePart -> {
            String filename = filePart.filename();
            log.debug("post save filename:{}", filename);
            return filePart.transferTo(Paths.get(filename));
        });
    }

//    @PostMapping(value = FILE)
//    public Mono<ResponseEntity<File>> post(@Valid @RequestBody File file) {
//        return fileRepository.save(file).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @PutMapping(value = FILE__ID__)
//    public Mono<ResponseEntity<File>> putById(@PathVariable(value = ID) String id, @Valid @RequestBody File file) {
//        // TODO need update policy
//        return fileRepository.save(file).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @GetMapping(value = FILE__ID__)
//    public Mono<ResponseEntity<File>> getById(@PathVariable(value = ID) String id) {
//        return fileRepository.findById(UUID.fromString(id)).map(ResponseEntity::ok)
//                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @DeleteMapping(value = FILE__ID__)
//    public Mono<ResponseEntity<Void>> delete(@PathVariable(value = ID) String id) {
//        return fileRepository.deleteById(UUID.fromString(id)).map(ResponseEntity::ok);
//    }
}
