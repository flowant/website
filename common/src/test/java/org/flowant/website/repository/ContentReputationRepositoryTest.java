package org.flowant.website.repository;

import org.flowant.website.WebSiteConfig;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentReputationRepositoryTest extends ReputationRepositoryTest<ContentReputation, ContentReputationRepository> {

    @Before
    public void before() {
        setDeleter(entity -> repo.deleteTestdataWithRelationship(entity).subscribe());
    }

    @Test
    public void testAccumulation() {
        super.testAccumulation(ContentReputation::getIdCid, ContentReputation::of);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReputationMaker.randomContentReputation(), ContentReputation::getContainerId);
    }

    @Test
    public void testPopularSubItems() {
        IdCid idCid = IdCid.random();
        super.popularSubItems(WebSiteConfig.getMaxSubItems(ContentReputation.class),
                idCid, ContentReputation::of);
    }

}
