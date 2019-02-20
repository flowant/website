package org.flowant.website.repository;

import org.flowant.website.model.ReplyReputation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.mapping.MapId;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyReputationRepositoryTest extends
        RepositoryTest<ReplyReputation, MapId, ReplyReputationRepository> {

    @Before
    public void before() {
        setDeleter(entity -> repo.deleteWithParent(entity).subscribe());
    }

    @Test
    public void testAccumulation() {
        super.testAccumulation(ReplyReputation::getMapId, ReplyReputation::of);
    }

}
