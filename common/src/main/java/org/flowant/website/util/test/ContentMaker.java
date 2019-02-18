package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Content;
import org.flowant.website.model.Recipe;
import org.flowant.website.model.Tag;

import com.datastax.driver.core.utils.UUIDs;

public class ContentMaker {

    static UUID containerId = UUIDs.timeBased();
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

    static UUID replierId = UUIDs.timeBased();
    static String replierName = "replierName";

    static int rating = 10;
    static int liked = 200;
    static int reported = 1000;

    static String name = "name";

    public static Content small(UUID id) {
        return Content.of(id, containerId, title + id, CRUZonedTime.now());
    }

    public static Content smallRandom() {
        return small(UUIDs.timeBased());
    }

    public static Content large(UUID id) {
        int cs = id.hashCode() / 1000000;
        return Content.builder().identity(id).containerId(containerId).title(title + id)
                .extend(new Recipe(List.of(ingredients + id, ingredients + id, ingredients + id),
                        prepareTime, cookTime, servings + cs, calory + cs, nutritionFacts + id))
                .fileRefs(List.of(FileMaker.largeRandom()))
                .sentences(sentences + id)
                .tags(List.of(Tag.of(name + id), Tag.of(name + id + 1), Tag.of(name + id + 2)))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static Content largeRandom() {
        return large(UUIDs.timeBased());
    }

}
