package org.flowant.website.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.IdScore;
import org.flowant.website.model.SubItem;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class SubItemRepositoryTest extends RepositoryTest<SubItem, UUID, SubItemRepository> {

    @Test
    public void crud() {

        SubItem subItem = SubItem.of(IdMaker.randomUUID(),
                    List.of(IdScore.of(IdMaker.randomUUID(), ReputationMaker.randomReputation().toScore())));

        testCrud(SubItem::getIdentity,
                () -> subItem,
                () -> subItem);
    }

    @Test
    public void addAndRemoveSubItem() {
        SubItem subItem = SubItem.of(IdMaker.randomUUID(), List.of(IdScore.random()));

        repo.save(subItem).block();
        cleaner.registerToBeDeleted(subItem);

        IdScore idScore = IdScore.random();

        repo.addSubItem(subItem.getIdentity(), idScore).block();

        SubItem found = repo.findById(subItem.getIdentity()).block();
        assertTrue(found.getSubItems().contains(idScore));

        repo.removeSubItem(subItem.getIdentity(), idScore).block();
        found = repo.findById(subItem.getIdentity()).block();
        assertFalse(found.getSubItems().contains(idScore));
    }

}
