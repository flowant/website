package org.flowant.website.repository;

import java.util.Comparator;
import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.Notification;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.NotificationMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class NotificationRepositoryTest extends IdCidRepositoryTest<Notification, NotificationRepository> {

    @Test
    public void crud() {
        testCrud(Notification::getIdCid, NotificationMaker::smallRandom, NotificationMaker::largeRandom);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Notification> entities = Flux.range(1, 10).map(i -> NotificationMaker.smallRandom(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void ordered() {
        testOrdered(Notification::getIdCid, Comparator.comparing(Notification::getIdentity).reversed(),
                NotificationMaker::smallRandom);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(NotificationMaker.largeRandom(), Notification::getContainerId);
    }

    @Test
    public void findAllBySubscriberIdPageable() {
        UUID subscriber = IdMaker.randomUUID();
        Flux<Notification> notifications = Flux.range(1, 10)
                .map(i -> NotificationMaker.largeRandom().setSubscribers(Set.of(subscriber))).cache();
        cleaner.registerToBeDeleted(notifications);
        saveAndGetPaging(notifications, pageable -> repo.findAllBySubscriberId(subscriber, pageable));
    }

}
