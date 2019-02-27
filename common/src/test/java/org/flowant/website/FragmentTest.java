package org.flowant.website;

import java.util.Comparator;
import java.util.List;

import org.flowant.website.model.IdScore;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.junit.Test;

import reactor.core.publisher.Flux;

public class FragmentTest {

    @Test
    public void contextLoads() {
        List<IdScore> idScores = Flux.range(1, 3)
                .map(i -> ReputationMaker.randomContentReputation().toIdScore())
                .log()
                .collectList()
                .block();

//        IdScore newIdScore = ReputationMaker.randomContentReputation().toIdScore();
        IdScore newIdScore = IdScore.of(IdMaker.randomUUID(), Long.MIN_VALUE);
        System.out.println("candidate:" + newIdScore);
        
        updatePopularSubItems(3, idScores, newIdScore);
    }

    public void updatePopularSubItems(int maxSize, List<IdScore> idScores, IdScore candidate) {
        if (idScores.size() < maxSize) {
            System.out.println("add candidate and return");
            //add query
            return;
        }

        idScores.add(candidate);
        idScores.sort(Comparator.comparing(IdScore::getScore).reversed());
        List<IdScore> toBeDeleted = idScores.subList(maxSize, idScores.size());

        if (toBeDeleted.contains(candidate)) {
            System.out.println("candidate cannot be added and return");
            // The most case are handled here, intended do nothing although list contains more than one elements.
            return;
        } else {
            // add candidate
            // remove query
            toBeDeleted.forEach(c -> System.out.println("candidate is added and remove : " + c));
        }

        return;
    }

}
