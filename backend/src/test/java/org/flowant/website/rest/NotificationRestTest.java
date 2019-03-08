package org.flowant.website.rest;

import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.flowant.website.repository.NotificationRepository;
import org.flowant.website.util.test.NotificationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class NotificationRestTest extends RestWithRepositoryTest<Notification, IdCid, NotificationRepository> {

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

}
