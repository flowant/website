package org.flowant.website.repository;

import org.flowant.website.model.ReviewReputation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReviewReputationRepositoryTest extends RepositoryTest<ReviewReputation, MapId, ReviewReputationRepository> {

    @Before
    public void before() {
        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
    }

    @Test
    public void testAccumulation() {
        super.testAccumulation(ReviewReputation::getMapId, ReviewReputation::of);
    }

}
