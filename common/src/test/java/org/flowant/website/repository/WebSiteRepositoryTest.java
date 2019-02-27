package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.WebSite;
import org.flowant.website.util.IdMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class WebSiteRepositoryTest extends RepositoryTest<WebSite, UUID, WebSiteRepository> {

    @Test
    public void crud() {
        WebSite webSite = WebSite.of(IdMaker.randomUUID());
        testCrud(WebSite::getIdentity, () -> webSite, () -> webSite);
    }

}
