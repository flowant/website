package org.flowant.website.util.test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Recipe;
import org.flowant.website.util.IdMaker;

public class ContentMaker {

    static String title = "title";

    static String ingredients = "ingredients";
    static String prepareTime = "10m20s";
    static String cookTime = "35m";
    static int servings = 0;
    static int calory = 1000; //TODO calculate with servings
    static String nutritionFacts = "nutritionFacts";

    static String url = "url";

    static String sentences = "sentences";
    static String content = "content";

    static UUID replierId = IdMaker.randomUUID();
    static String replierName = "replierName";

    static int rating = 10;
    static int liked = 200;
    static int reported = 1000;

    static String tag = "tag";

    public static Content small(IdCid idCid) {
        UUID id = idCid.getIdentity();
        return Content.of(idCid, title + id, ReputationMaker.emptyReputation(), CRUZonedTime.now());
    }

    public static Content smallRandom(UUID containerId) {
        return small(IdCid.random(containerId));
    }

    public static Content smallRandom() {
        return small(IdCid.random());
    }

    public static Content large(IdCid idCid) {
        UUID id = idCid.getIdentity();
        int cs = id.hashCode() / 1000000;
        return Content.builder()
                .idCid(idCid)
                .title(title + id)
                .extend(new Recipe(List.of(ingredients + id, ingredients + id, ingredients + id),
                        prepareTime, cookTime, servings + cs, calory + cs, nutritionFacts + id))
                .fileRefs(List.of(FileMaker.largeRandom()))
                .sentences(sentences + id)
                .tags(Set.of(tag + id, tag + id + 1, tag + id + 2))
                .reputation(ReputationMaker.emptyReputation())
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static Content largeRandom(UUID containerId) {
        return large(IdCid.random(containerId));
    }

    public static Content largeRandom() {
        return large(IdCid.random());
    }

}
