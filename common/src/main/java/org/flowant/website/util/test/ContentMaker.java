package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Content;
import org.flowant.website.model.Recipe;
import org.flowant.website.model.Tag;
import org.springframework.data.cassandra.core.mapping.MapId;

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

    static String name = "name";

    public static Content small(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        return Content.of(id, IdMaker.toContainerId(mapId), title + id, CRUZonedTime.now());
    }

    public static Content smallRandom() {
        return small(IdMaker.randomMapId());
    }

    public static Content large(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        int cs = id.hashCode() / 1000000;
        return Content.builder().identity(id).containerId(IdMaker.toContainerId(mapId)).title(title + id)
                .extend(new Recipe(List.of(ingredients + id, ingredients + id, ingredients + id),
                        prepareTime, cookTime, servings + cs, calory + cs, nutritionFacts + id))
                .fileRefs(List.of(FileMaker.largeRandom()))
                .sentences(sentences + id)
                .tags(List.of(Tag.of(name + id), Tag.of(name + id + 1), Tag.of(name + id + 2)))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static Content largeRandom() {
        return large(IdMaker.randomMapId());
    }

}
