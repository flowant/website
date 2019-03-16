package org.flowant.website.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Category;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.flowant.website.model.Relation;
import org.flowant.website.model.ZonedTime;
import org.flowant.website.repository.NotificationRepository;
import org.flowant.website.repository.RelationRepository;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.NotificationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class NotificationRestTest extends RestWithRepositoryTest<Notification, IdCid, NotificationRepository> {

    @Autowired
    RelationRepository repoRelation;

    @Before
    public void before() {
        super.before();

        setTestParams(NotificationRest.NOTIFICATION, Notification.class, Notification::getIdCid,
                NotificationMaker::smallRandom, NotificationMaker::largeRandom,
                r -> r.setAppendix("new appendix"));
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testPagination() {
        Function<UUID, Notification> supplier = (containerId) -> NotificationMaker.largeRandom(containerId);
        pagination(10, 1, supplier);
        pagination(10, 3, supplier);
        pagination(10, 5, supplier);
        pagination(10, 11, supplier);
    }

    @Test
    public void testNewContentNotification() {

        UUID userId = IdMaker.randomUUID();
        UUID follower = IdMaker.randomUUID();

        Relation relation = Relation.of(userId, Set.of(), Set.of(follower));
        repoRelation.save(relation).block();

        Notification noti = Notification.of(IdCid.random(userId), "authorName", Category.NC, ZonedTime.now()).setSubscribers(Set.of(IdMaker.randomUUID()));
        cleaner.registerToBeDeleted(noti);

        setDeleter(n -> repo.deleteById(n.getIdCid()).then(repoRelation.deleteById(n.getContainerId())).subscribe());

        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(noti), entityClass)
                .exchange()
                .expectStatus().isOk()
                .expectBody(entityClass).consumeWith(r -> {
                    log.trace(r);
                    Notification n = repo.findById(noti.getIdCid()).block();
                    assertTrue(n.getSubscribers().contains(follower));
                });
    }

    @Test
    public void testRemoveSubscriber() {
        Notification noti = NotificationMaker.largeRandom();
        UUID subscriber = IdMaker.randomUUID();
        noti.setSubscribers(Set.of(subscriber));
        repo.save(noti).block();
        cleaner.registerToBeDeleted(noti);

        assertTrue(repo.findById(noti.getIdCid()).block().getSubscribers().contains(subscriber));

        webTestClient.delete()
                .uri(baseUrl + "/" + noti.getIdentity() + "/" + noti.getContainerId() + "/" + subscriber)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(r -> {
                    log.trace(r::toString);
                    assertFalse(repo.findById(noti.getIdCid()).block().getSubscribers().contains(subscriber));
                });
    }

}
