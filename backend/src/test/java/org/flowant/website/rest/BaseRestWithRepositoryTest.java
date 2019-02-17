package org.flowant.website.rest;

import static org.flowant.website.BackendSecurityConfiguration.ROLE_WRITER;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import java.util.function.Function;
import java.util.function.Supplier;

import org.flowant.website.model.Tag;
import org.flowant.website.util.test.AssertUtil;
import org.flowant.website.util.test.DeleteAfterTest;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.datastax.driver.core.utils.UUIDs;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
public class BaseRestWithRepositoryTest <Entity, ID, Repository extends ReactiveCrudRepository<Entity, ID>> 
    extends DeleteAfterTest <Entity, ID, Repository> {

    public final static String __ID__ = "/{id}";
    public final static String SCHEME = "http";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    ApplicationContext context;

    WebTestClient webTestClient;

    @Value("${server.address}")
    protected String host;

    @LocalServerPort
    protected int port;

    String baseUrl;
    Class<Entity> entityClass;
    Function<Entity, ID> getEntityId;
    Supplier<Entity> smallEntity;
    Supplier<Entity> largeEntity;
    Function<Entity, Entity> modifyEntity;

    @Before
    public void before() {
        webTestClient = WebTestClient
            .bindToApplicationContext(context)
            .apply(springSecurity())
            .configureClient()
            .build().mutateWith(csrf())
            .mutateWith(mockUser().roles(ROLE_WRITER));
    }

    @After
    public void after() {
        deleteRegistered();
    }

    public void testCrud(String baseUrl, Class<Entity> entityClass, Function<Entity, ID> getEntityId, Supplier<Entity> smallEntity,
            Supplier<Entity> largeEntity, Function<Entity, Entity> modifyEntity) {
        this.baseUrl = baseUrl;
        this.entityClass = entityClass;
        this.getEntityId = getEntityId;
        this.smallEntity = smallEntity;
        this.largeEntity = largeEntity;
        this.modifyEntity = modifyEntity;

        testInsertMalformed();
        testInsert(smallEntity.get());
        testInsert(largeEntity.get());
        testGetNotExist();
        testGetMalformedId();
        testGetId(smallEntity.get());
        testGetId(largeEntity.get());
        testPut(smallEntity.get());
        testDelete(smallEntity.get());
        testDelete(largeEntity.get());
    }

    public void testInsertMalformed() {
        ResponseSpec respSpec = webTestClient.post().uri(baseUrl).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(Tag.of("notEntity")), Tag.class)
                .exchange();
        respSpec.expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    public void testInsert(Entity entity) {
        registerToBeDeleted(entity);

        webTestClient.post().uri(baseUrl).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(entity), entityClass)
                .exchange()
                .expectStatus().isOk().expectBody(entityClass).consumeWith(r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(entity, r.getResponseBody());
                    AssertUtil.assertEquals(entity, repo.findById(getEntityId.apply(entity)).block());
                });
    }

    public void testGetNotExist() {
        webTestClient.get().uri(baseUrl + __ID__, UUIDs.timeBased())
                .exchange()
                .expectStatus().isNotFound().expectBody().consumeWith(log::trace);
    }

    public void testGetMalformedId() {
        webTestClient.get().uri(baseUrl + __ID__, "notExist")
                .exchange()
                .expectStatus().is5xxServerError().expectBody().consumeWith(log::trace);
    }

    public void testGetId(Entity entity) {
        repo.save(entity).block();
        registerToBeDeleted(entity);

        webTestClient.get().uri(baseUrl + __ID__, getEntityId.apply(entity)).accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(entityClass).consumeWith( r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(entity, r.getResponseBody());
                });
    }

    public void testPut(Entity entity) {
        repo.save(entity).block();
        Entity modifiedEntity = modifyEntity.apply(entity);
        registerToBeDeleted(modifiedEntity);

        webTestClient.put().uri(baseUrl).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(modifiedEntity), entityClass)
                .exchange()
                .expectStatus().isOk().expectBody(entityClass).consumeWith( r -> {
                    log.trace(r::toString);
                    AssertUtil.assertEquals(modifiedEntity, r.getResponseBody());
                    AssertUtil.assertEquals(modifiedEntity, repo.findById(getEntityId.apply(entity)).block());
                });
    }

    public void testDelete(Entity entity) {
        repo.save(entity).block();
        registerToBeDeleted(entity); // in case of fails

        webTestClient.delete().uri(baseUrl + __ID__, getEntityId.apply(entity))
                .exchange()
                .expectStatus().isOk().expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(repo.findById(getEntityId.apply(entity)))
                            .expectNextCount(0).verifyComplete();
                });
    }
}
