package org.flowant.website.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.FileRef;
import org.flowant.website.rest.FileRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class FileStorage {
    public static final String SEP_URL = "/";
    public static final String SEP_DIR = "/";

    static String root;

    @Value("${website.storage.root}")
    public void createPathIfNotExist(String root) throws IOException {
        FileStorage.root = root;
        mkdirs(root);
        log.debug("Storage's root path is created:{}", root);
    }

    public static Flux<FileRef> saveAll(Flux<FilePart> files) {
        return files.flatMap(partFile -> {
            UUID id = UUID.randomUUID();
            FileRef fileRef = FileRef.builder()
                    .id(id).cruTime(CRUZonedTime.now())
                    .contentType(partFile.headers().getContentType().toString())
                    .length(partFile.headers().getContentLength())
                    .filename(partFile.filename()).uri(FileRest.FILES + SEP_URL + id)
                    .build();
            Path path = Paths.get(root + SEP_DIR + id);
            return partFile.transferTo(path).cast(FileRef.class)
            .concatWith(Mono.just(fileRef));
        });
    }

    public static Flux<Resource> findAll () throws IOException {
        return Flux.fromStream(listFiles(root)).map(path -> new FileSystemResource(path));
    }

    public static Mono<Resource> findById (String id) {
        return exist(id) ? Mono.just(new FileSystemResource(root + SEP_DIR + id)) : Mono.empty();
    }

    public static Mono<Resource> findById (UUID id) {
        return findById(id.toString());
    }

    public static Mono<Boolean> deleteById(String id) {
        return exist(id) == false ? Mono.empty() :
                Mono.fromCallable(() -> rmdirs(root + SEP_DIR + id)).onErrorResume(t -> {
                    log.error(ExceptionUtils.getStackTrace(t));
                    return Mono.just(Boolean.FALSE);
                });
    }

    public static Mono<Boolean> deleteById(UUID id) {
        return deleteById(id.toString());
    }

    public static Path mkdirs(String path) throws IOException {
        return mkdirs(Paths.get(path));
    }

    public static boolean exist(String id) {
        return Files.exists(Paths.get(root + SEP_DIR + id));
    }

    public static Path mkdirs(Path path) throws IOException {
        if (Files.exists(path) == false)
            Files.createDirectory(path);
        return path;
    }

    public static boolean rmdirs(String path) throws IOException {
        return rmdirs(Paths.get(path));
    }

    public static boolean rmdirs(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

    public static Stream<String> listFiles(String path) throws IOException {
        return listFiles(Paths.get(path)).map(Path::toString);
    }

    public static Stream<Path> listFiles(Path path) throws IOException {
        return Files.list(path).filter(Files::isRegularFile);
    }
}
