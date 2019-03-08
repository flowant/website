package org.flowant.website.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.Relation;
import org.flowant.website.util.IdMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class RelationRepositoryTest extends RepositoryTest<Relation, UUID, RelationRepository> {

    @Test
    public void crud() {

        Relation relation = Relation.of(IdMaker.randomUUID(), Set.of(IdMaker.randomUUID()), Set.of(IdMaker.randomUUID()));

        testCrud(Relation::getIdentity,
                () -> relation,
                () -> relation);
    }

    @Test
    public void followAndUnfollow() {
        UUID follower = IdMaker.randomUUID();
        UUID followee = IdMaker.randomUUID();

        Relation followerRelation = Relation.of(follower, Set.of(), Set.of());
        Relation followeeRelation = Relation.of(followee, Set.of(), Set.of());

        repo.save(followerRelation).block();
        repo.save(followeeRelation).block();
        cleaner.registerToBeDeleted(followerRelation);
        cleaner.registerToBeDeleted(followeeRelation);

        repo.follow(follower, followee).block();
        Relation found = repo.findById(follower).block();
        assertTrue(found.getFollowings().contains(followee));

        found = repo.findById(followee).block();
        assertTrue(found.getFollowers().contains(follower));

        repo.unfollow(follower, followee).block();
        found = repo.findById(follower).block();
        assertFalse(found.getFollowings().contains(followee));

        found = repo.findById(followee).block();
        assertFalse(found.getFollowers().contains(follower));
    }

}
