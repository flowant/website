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
        UUID man = IdMaker.randomUUID();
        UUID woman = IdMaker.randomUUID();

        Relation manRelation = Relation.of(man, Set.of(), Set.of());
        Relation womanRelation = Relation.of(woman, Set.of(), Set.of());

        repo.save(manRelation).block();
        repo.save(womanRelation).block();
        cleaner.registerToBeDeleted(manRelation);
        cleaner.registerToBeDeleted(womanRelation);

        repo.follow(man, woman).block();
        Relation found = repo.findById(man).block();
        assertTrue(found.getFollowings().contains(woman));

        found = repo.findById(woman).block();
        assertTrue(found.getFollowers().contains(man));

        repo.unfollow(man, woman).block();
        found = repo.findById(man).block();
        assertFalse(found.getFollowings().contains(woman));

        found = repo.findById(woman).block();
        assertFalse(found.getFollowers().contains(man));
    }

}
