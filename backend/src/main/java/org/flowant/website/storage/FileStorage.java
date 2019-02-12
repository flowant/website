package org.flowant.website.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.FileRef;
import org.flowant.website.rest.FileRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
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

    static TaskExecutor taskExecutor = taskExecutor();

    @Bean
    static TaskExecutor taskExecutor () {
        return new ConcurrentTaskExecutor();
    }

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
            File file = getPath(id).toFile();
            return partFile.transferTo(file).cast(FileRef.class)
                    .concatWith(Mono.just(fileRef.setLength(file.length())));
        });
    }

    public static Flux<Resource> findAll () throws IOException {
        return Flux.fromStream(listFiles(root)).map(path -> new FileSystemResource(path));
    }

    public static Mono<Resource> findById (String id) {
        return exist(id) ? Mono.just(new FileSystemResource(getPath(id))) : Mono.empty();
    }

    public static Mono<Resource> findById (UUID id) {
        return findById(id.toString());
    }

    public static Mono<Boolean> deleteById(String id) {
        return exist(id) == false ? Mono.empty() :
                Mono.fromCallable(() -> rmdirs(getPath(id))).onErrorResume(t -> {
                    log.error(ExceptionUtils.getStackTrace(t));
                    return Mono.just(Boolean.FALSE);
                });
    }

    public static Mono<Boolean> deleteById(UUID id) {
        return deleteById(id.toString());
    }

    public static Mono<Boolean> deleteAll(Flux<FileRef> files) {
        return files.collectList().flatMap(FileStorage::deleteAll);
    }

    public static Mono<Boolean> deleteAll(List<FileRef> files) {
        taskExecutor.execute(() -> {
            files.forEach(fileRef -> {
                String id = fileRef.getId().toString();
                if (exist(id)) {
                    try {
                        rmdirs(getPath(id));
                        log.debug("file is deleted, id: {}", id);
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            });
        });

        return Mono.just(Boolean.TRUE);
    }

    public static Path getPath(UUID id) {
        return getPath(id.toString());
    }

    public static Path getPath(String id) {
        return Paths.get(root + SEP_DIR + id);
    }

    public static Path mkdirs(String path) throws IOException {
        return mkdirs(Paths.get(path));
    }

    public static boolean exist(UUID id) {
        return exist(id.toString());
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
