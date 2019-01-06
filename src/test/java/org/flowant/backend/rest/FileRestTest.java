package org.flowant.backend.rest;

import org.flowant.backend.model.FileRef;
import org.flowant.backend.model.Tag;
import org.flowant.backend.storage.FileStorage;
import org.junit.Assert;
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
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

    @Test
    public void testPostMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(FileRest.FILES).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notFile")), Tag.class).exchange();
        respSpec.expectStatus().is4xxClientError().expectBody().consumeWith(log::trace);
    }

    @Test
    @Parameters({"1", "3"})
    public void testPosts(int count) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        for (int i = 1; i <= count; i++) {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ClassPathResource> entity = new HttpEntity<>(
                    new ClassPathResource("sea" + i + ".jpg"), headers);
            parts.add(FileRest.ATTACHMENT, entity);
        }

        webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(parts)).exchange()
        .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(count).consumeWith(body -> {
            body.getResponseBody().forEach(fileRef -> {
                log.trace("post files response:{}", fileRef);
                StepVerifier.create(FileStorage.findById(fileRef.getId()))
                        .expectNextCount(1).verifyComplete();
                FileStorage.deleteById(fileRef.getId()).subscribe();
            });
        });
    }

    @Test
    public void testGetId() {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        ClassPathResource img = new ClassPathResource("sea1.jpg");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ClassPathResource> entity = new HttpEntity<>(img, headers);
        parts.add(FileRest.ATTACHMENT, entity);

        webTestClient.post().uri(FileRest.FILES).contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(parts)).exchange()
        .expectStatus().isOk().expectBodyList(FileRef.class).hasSize(1).consumeWith(body -> {
            body.getResponseBody().forEach(fileRef -> {
                webTestClient.get().uri(FileRest.FILES__ID__, fileRef.getId()).exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith( s -> {
                    Assert.assertTrue(0 < s.getResponseBodyContent().length);
                    FileStorage.deleteById(fileRef.getId()).subscribe();
                });
            });
        });
    }

// TODO error cases

//  @Test
//  public void testDelete() {
//      File file = FileTest.large();
//      fileRepository.save(file).block();
//
//      webTestClient.delete().uri(FileRest.FILE__ID__, file.getId()).exchange()
//              .expectStatus().isOk().expectBody().consumeWith(r -> {
//                  log.trace(r::toString);
//                  StepVerifier.create(fileRepository.findById(file.getId())).expectNextCount(0).verifyComplete();
//              });
//  }
    
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


}
