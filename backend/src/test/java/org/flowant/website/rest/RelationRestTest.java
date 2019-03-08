package org.flowant.website.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Relation;
import org.flowant.website.repository.RelationRepository;
import org.flowant.website.util.IdMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class RelationRestTest extends RestWithRepositoryTest<Relation, UUID, RelationRepository> {

    @Before
    public void before() {
        super.before();

        Relation relation = Relation.of(IdMaker.randomUUID(), Set.of(), Set.of());

        setTestParams(RelationRest.RELATION, Relation.class, Relation::getIdentity,
                () -> relation, () -> relation, s -> s);
    }

    @Test
    public void testCrud() {
        super.testCrud();
    }

    @Test
    public void testFollow() {

        UUID follower = IdMaker.randomUUID();
        UUID followee = IdMaker.randomUUID();
        Relation followerRelation = Relation.of(follower, Set.of(), Set.of());
        Relation followeeRelation = Relation.of(followee, Set.of(), Set.of());
        repo.save(followerRelation).block();
        repo.save(followeeRelation).block();
        cleaner.registerToBeDeleted(followerRelation);
        cleaner.registerToBeDeleted(followeeRelation);

        webTestClient.post()
                .uri(baseUrl + "/" + RelationRest.FOLLOW + "/" + follower + "/" + followee)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(entityClass).consumeWith(r -> {
                    log.trace(r);
                    Relation found = repo.findById(follower).block();
                    assertTrue(found.getFollowings().contains(followee));

                    found = repo.findById(followee).block();
                    assertTrue(found.getFollowers().contains(follower));
                });

        webTestClient.post()
                .uri(baseUrl + "/" + RelationRest.UNFOLLOW + "/" + follower + "/" + followee)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(entityClass).consumeWith(r -> {
                    log.trace(r);
                    Relation found = repo.findById(follower).block();
                    assertFalse(found.getFollowings().contains(followee));

                    found = repo.findById(followee).block();
                    assertFalse(found.getFollowers().contains(follower));
                });

    }

}
