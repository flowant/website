package org.flowant.website.rest;

import java.util.List;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.IdScore;
import org.flowant.website.model.SubItem;
import org.flowant.website.repository.SubItemRepository;
import org.flowant.website.util.IdMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class SubItemRestTest extends RestWithRepositoryTest<SubItem, UUID, SubItemRepository> {

    @Before
    public void before() {
        super.before();

        SubItem subItem = SubItem.of(IdMaker.randomUUID(), List.of(IdScore.random()));

        setTestParams(SubItemRest.PATH_SUBITEM, SubItem.class, SubItem::getIdentity,
                () -> subItem, () -> subItem, s -> s);
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
