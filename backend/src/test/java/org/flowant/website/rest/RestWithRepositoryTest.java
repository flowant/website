package org.flowant.website.rest;

import static org.flowant.website.BackendSecurityConfiguration.ROLE_WRITER;
import static org.flowant.website.rest.IdCidRepositoryRest.CID;
import static org.flowant.website.rest.IdCidRepositoryRest.PAGE;
import static org.flowant.website.rest.IdCidRepositoryRest.SIZE;
import static org.flowant.website.rest.PopularRepositoryRest.POPULAR;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.repository.ReputationRepository;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.AssertUtil;
import org.flowant.website.util.test.DeleteAfterTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.datastax.driver.core.utils.UUIDs;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
public abstract class RestWithRepositoryTest <Entity, ID, Repository extends ReactiveCassandraRepository<Entity, ID>> {

    public final static String __ID__ = "/{id}";
    public final static String SCHEME = "http";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    Repository repo;

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

    protected DeleteAfterTest<Entity> cleaner = new DeleteAfterTest<>();

    Consumer<Entity> deleter = entity -> repo.delete(entity).subscribe();

    public void setDeleter(Consumer<Entity> deleter) {
        this.deleter = deleter;
    }

    @After
    public void after() {
        cleaner.deleteRegistered(deleter);
    }

    @Before
    public void before() {
        webTestClient = WebTestClient
            .bindToApplicationContext(context)
            .apply(springSecurity())
            .configureClient()
            .build().mutateWith(csrf())
            .mutateWith(mockUser().roles(ROLE_WRITER));
    }

    public void setTestParams(String baseUrl, Class<Entity> entityClass, Function<Entity, ID> getEntityId, Supplier<Entity> smallEntity,
            Supplier<Entity> largeEntity, Function<Entity, Entity> modifyEntity) {

        this.baseUrl = baseUrl;
        this.entityClass = entityClass;
        this.getEntityId = getEntityId;
        this.smallEntity = smallEntity;
        this.largeEntity = largeEntity;
        this.modifyEntity = modifyEntity;
    }

    public void testCrud() {
        testInsertMalformed();
        testInsert(smallEntity.get());
        testInsert(largeEntity.get());
        testGetNotExist();
        //TODO handle error
        //testGetMalformedId();
        testGetId(smallEntity.get());
        testGetId(largeEntity.get());
        testPut(smallEntity.get());
        testDelete(smallEntity.get());
        testDelete(largeEntity.get());
    }

    public URI getUri(Entity entity) {
        ID id = getEntityId.apply(entity);
        UriBuilder builder = UriComponentsBuilder.fromPath(baseUrl);
        return UriUtil.getUri(id, builder);
    }

    public void testInsertMalformed() {
        ResponseSpec respSpec = webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just("notEntity"), String.class)
                .exchange();

        respSpec.expectStatus().is4xxClientError()
                .expectBody().consumeWith(log::trace);
    }

    public void testInsert(Entity entity) {
        cleaner.registerToBeDeleted(entity);

        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(entity), entityClass)
                .exchange()
                .expectStatus().isOk()
                .expectBody(entityClass).consumeWith(r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(entity, r.getResponseBody());
                    AssertUtil.assertEquals(entity, repo.findById(getEntityId.apply(entity)).block());
                });
    }

    public void testGetNotExist() {
        webTestClient.get()
                .uri(baseUrl + __ID__, UUIDs.timeBased())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().consumeWith(log::trace);
    }

    public void testGetMalformedId() {
        webTestClient.get()
                .uri(baseUrl + __ID__, "notExist")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().consumeWith(log::trace);
    }

    public void testGetId(Entity entity) {
        repo.save(entity).block();
        cleaner.registerToBeDeleted(entity);

        webTestClient.get()
                .uri(getUri(entity))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(entityClass).consumeWith( r -> {
                    log.trace(r);
                    AssertUtil.assertEquals(entity, r.getResponseBody());
                });
    }

    public void testPut(Entity entity) {
        repo.save(entity).block();
        Entity modifiedEntity = modifyEntity.apply(entity);
        cleaner.registerToBeDeleted(modifiedEntity);

        webTestClient.put()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(modifiedEntity), entityClass)
                .exchange()
                .expectStatus().isOk()
                .expectBody(entityClass).consumeWith( r -> {
                    log.trace(r::toString);
                    AssertUtil.assertEquals(modifiedEntity, r.getResponseBody());
                    AssertUtil.assertEquals(modifiedEntity, repo.findById(getEntityId.apply(entity)).block());
                });
    }

    public void testDelete(Entity entity) {
        repo.save(entity).block();
        cleaner.registerToBeDeleted(entity); // in case of fails

        webTestClient.delete()
                .uri(getUri(entity))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    StepVerifier.create(repo.findById(getEntityId.apply(entity))).expectNextCount(0).verifyComplete();
                });
    }

    public void pagination(int cntEntities, int pageSize, Function<UUID, Entity> supplier) {
        UUID containerId = UUIDs.timeBased();

        Assert.assertTrue(repo instanceof ReputationRepository);

        Flux<Entity> contents = Flux.range(1, cntEntities).map(i -> supplier.apply(containerId)).cache();
        repo.saveAll(contents).blockLast();
        cleaner.registerToBeDeleted(contents);

        ClientResponse resp = WebClient.create().get()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(baseUrl)
                        .queryParam(CID, containerId.toString())
                        .queryParam(PAGE, "0")
                        .queryParam(SIZE, String.valueOf(pageSize)).build())
                .exchange().block();

        List<Entity> list = new ArrayList<>();
        Optional<URI> nextUrl;

        while(true) {
            list.addAll(resp.bodyToFlux(entityClass).collectList().block());
            nextUrl = LinkUtil.getNextUrl(resp.headers().asHttpHeaders());
            if (nextUrl.isEmpty()) {
                break;
            }
            log.trace("nextUrl:{}", nextUrl);
            resp = WebClient.create().get().uri(nextUrl.get()).exchange().block();
        }
        Assert.assertTrue(contents.all(c -> list.contains(c)).block());
    }

    public void popularSubItem(int maxSubItems, Function<UUID, Entity> supplier,
            Function<Flux<Entity>, Flux<ReputationCounter>> makeReputationAndSave) {

        UUID containerId = IdMaker.randomUUID();
        Flux<Entity> entities = Flux.range(1, maxSubItems * 2)
                .map(i -> supplier.apply(containerId))
                .cache();

        repo.saveAll(entities).blockLast();
        cleaner.registerToBeDeleted(entities);

        Flux<ReputationCounter> reputations = makeReputationAndSave.apply(entities);

        List<IdCid> expected = reputations
                .sort(Collections.reverseOrder(ReputationCounter::compare))
                .map(ReputationCounter::getIdCid)
                .limitRequest(maxSubItems)
                .collectList()
                .block();

        log.trace("expected:{}", expected);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(baseUrl + POPULAR)
                        .queryParam(CID, containerId.toString()).build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(entityClass)
                .consumeWith(r -> {
                    log.trace(r);
                    List<ID> actual = Flux.fromIterable(r.getResponseBody())
                            .map(getEntityId)
                            .collectList()
                            .block();
                    expected.forEach(idCid -> assertTrue(actual.contains(idCid)));
                });
    }

}
