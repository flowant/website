package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.repository.BackendContentReputationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ContentReputationRestTest extends BaseRestWithRepositoryTest<ContentReputation, UUID, BackendContentReputationRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ContentReputationRest.CONTENT_REPUTATION, ContentReputation.class, ContentReputation::getId,
                () -> ContentReputation.of(UUIDs.timeBased()), () -> ContentReputation.of(UUIDs.timeBased(), 1, 2, 3, 4, 5, 6),
                (ContentReputation cr) -> {
                    cr.setLiked(1);
                    return cr;
                });
    }

}
