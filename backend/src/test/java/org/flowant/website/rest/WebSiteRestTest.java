package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.WebSite;
import org.flowant.website.repository.WebSiteRepository;
import org.flowant.website.util.IdMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class WebSiteRestTest extends RestWithRepositoryTest<WebSite, UUID, WebSiteRepository> {

    @Before
    public void before() {
        super.before();

        WebSite webSite = WebSite.of(IdMaker.randomUUID());

        setTestParams(WebSiteRest.PATH_WEBSITE, WebSite.class, WebSite::getIdentity,
                () -> webSite, () -> webSite, w -> w);
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
