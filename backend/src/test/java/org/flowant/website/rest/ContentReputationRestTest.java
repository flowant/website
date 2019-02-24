package org.flowant.website.rest;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class ContentReputationRestTest extends RestWithRepositoryTest<ContentReputation, IdCid, ContentReputationRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(ContentReputationRest.CONTENT_REPUTATION,
                ContentReputation.class,
                ContentReputation::getIdCid,
                ReputationMaker::emptyContentReputation,
                ReputationMaker::randomContentReputation,
                cr -> cr.setLiked(1));

        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

}
