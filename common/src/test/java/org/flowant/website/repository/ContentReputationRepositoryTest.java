package org.flowant.website.repository;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.WebSite;
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
        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
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
    public void testPopularChildren() {
        super.popularChildren(5, 10, ReputationMaker::randomContentReputation, WebSite.class);
    }

}
