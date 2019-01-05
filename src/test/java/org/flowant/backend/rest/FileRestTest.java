package org.flowant.backend.rest;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

import org.flowant.backend.model.File;
import org.flowant.backend.model.Tag;
import org.flowant.backend.repository.FileRepository;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
public class FileRestTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private FileRepository fileRepository;

    Consumer<? super File> deleteFile = u -> fileRepository.delete(u).subscribe();
    Consumer<? super Collection<File>> deleteFiles = l -> l.forEach(deleteFile);

//    @Test
    public void testPostMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(FileRest.FILE).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notFile")), Tag.class).exchange();
        respSpec.expectStatus().is4xxClientError().expectBody().consumeWith(log::trace);
    }

    @Test
    public void testPost() {
        ClassPathResource img = new ClassPathResource("sea1.jpg");

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add(FileRest.CID, UUID.randomUUID().toString());

        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add(FileRest.ATTACHMENT, entity);

        webTestClient.post()
//                .uri(builder -> builder.path(FileRest.FILE)
//                        .queryParam(FileRest.CID, UUID.randomUUID()).build())
                .uri(FileRest.FILE)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange().expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
//                    StepVerifier.create(fileRepository.findById(file.getId()))
//                            .consumeNextWith(deleteFile).verifyComplete();
                });
    }

    @Test
    public void testPosts() {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        for (int i = 2; i < 4; i++) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(FileRest.CID, UUID.randomUUID().toString());

            HttpEntity<ClassPathResource> entity = new HttpEntity<>(
                    new ClassPathResource("sea" + i + ".jpg"), headers);

            parts.add(FileRest.ATTACHMENT, entity);
        }

        webTestClient.post()
//                .uri(builder -> builder.path(FileRest.FILE)
//                        .queryParam(FileRest.CID, UUID.randomUUID()).build())
                .uri(FileRest.FILES)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange().expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r);
//                    StepVerifier.create(fileRepository.findById(file.getId()))
//                            .consumeNextWith(deleteFile).verifyComplete();
                });
    }


//    @Test
//    public void testGetNotExist() {
//        webTestClient.get().uri(FileRest.FILE__ID__, UUID.randomUUID()).exchange()
//                .expectStatus().isNotFound().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    public void testGetMalformedId() {
//        webTestClient.get().uri(FileRest.FILE__ID__, "notExist").exchange()
//                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    @Parameters
//    public void testGetId(File file) {
//        fileRepository.save(file).block();
//        webTestClient.get().uri(FileRest.FILE__ID__, file.getId()).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(File.class).isEqualTo(file).consumeWith( r -> {
//                    log.trace(r);
//                    deleteFile.accept(file);
//                });
//    }
//    public static List<File> parametersForTestGetId() {
//        return Arrays.asList(FileTest.large());
//    }
//
//    @Test
//    public void testGetAllEmpty() {
//        fileRepository.deleteAll().block();
//        webTestClient.get().uri(FileRest.FILE).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBody(List.class).consumeWith(log::trace).isEqualTo(Lists.emptyList());
//    }
//
//    @Test
//    public void testGetAll() {
//        Flux<File> files = Flux.range(1, 5).map(FileTest::large).cache();
//        fileRepository.deleteAll().thenMany(fileRepository.saveAll(files)).blockLast();
//
//        webTestClient.get().uri(FileRest.FILE).accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .expectBodyList(File.class).hasSize(5).consumeWith(r -> {
//                    log.trace(r);
//                    files.subscribe(deleteFile);
//                });
//    }
//
//    @Test
//    public void testPutNotExist() {
//        File file = FileTest.large();
//        webTestClient.put().uri(FileRest.FILE__ID__, file).contentType(MediaType.APPLICATION_JSON_UTF8)
//        .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(file), File.class).exchange()
//        .expectStatus().isOk().expectBody().consumeWith(r -> {
//            log.trace(r);
//            StepVerifier.create(fileRepository.findById(file.getId()))
//                    .consumeNextWith(deleteFile).verifyComplete();
//        });
//    }
//
//    @Test
//    public void testPutMalformedId() {
//        webTestClient.put().uri(FileRest.FILE__ID__, "notExist").exchange()
//                .expectStatus().isBadRequest().expectBody().consumeWith(log::trace);
//    }
//
//    @Test
//    public void testPut() {
//        File file = FileTest.large();
//        fileRepository.save(file).block();
//
//        webTestClient.put().uri(FileRest.FILE__ID__, file).contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(file), File.class).exchange()
//                .expectStatus().isOk().expectBody().consumeWith( r -> {
//                    log.trace(r::toString);
//                    StepVerifier.create(fileRepository.findById(file.getId())).expectNext(file)
//                            .then(()-> deleteFile.accept(file)).verifyComplete();
//                });
//    }
//
//    @Test
//    public void testDelete() {
//        File file = FileTest.large();
//        fileRepository.save(file).block();
//
//        webTestClient.delete().uri(FileRest.FILE__ID__, file.getId()).exchange()
//                .expectStatus().isOk().expectBody().consumeWith(r -> {
//                    log.trace(r::toString);
//                    StepVerifier.create(fileRepository.findById(file.getId())).expectNextCount(0).verifyComplete();
//                });
//    }

}
