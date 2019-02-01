package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Content;
import org.flowant.website.model.Recipe;
import org.flowant.website.model.Reply;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.Tag;
import org.junit.Assert;

public class ContentMaker {
    static String title = "title";

    static String ingredients = "ingredients";
    static long prepareSeconds = 60;
    static long cookSeconds = 300;
    static int servings = 0;
    static int calory = 1000; //TODO calculate with servings
    static String nutritionFacts = "nutritionFacts";

    static String url = "url";

    static String sentences = "sentences";
    static String content = "content";

    static int rating = 10;
    static int liked = 200;
    static int reported = 1000;

    static String name = "name";

    public static Content small(UUID id) {
        int cs = id.hashCode() / 1000000;
        return Content.of(id, title + id,
                Review.of(Reputation.of(rating + cs, liked + cs, reported + cs)), CRUZonedTime.now());
    }

    public static Content smallRandom() {
        return small(UUID.randomUUID());
    }

    public static Content large(UUID id) {
        int cs = id.hashCode() / 1000000;
        return Content.builder().id(id).title(title + id)
                .extend(new Recipe(List.of(ingredients + id), prepareSeconds + cs, cookSeconds + cs,
                        servings + cs, calory + cs, nutritionFacts + id))
                .fileRefs(List.of(FileMaker.largeRandom()))
                .sentences(sentences + id)
                .tags(List.of(Tag.of(name + id)))
                .review(new Review(Reputation.of(rating + cs, liked + cs, reported + cs),
                        List.of(new Reply(content + id, Reputation.of(rating + cs, liked + cs, reported + cs),
                                CRUZonedTime.now()))))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static Content largeRandom() {
        return large(UUID.randomUUID());
    }

    public static Content assertEqual(Content excepted, Content actual) {
        Assert.assertEquals(excepted.getId(), actual.getId());
        Assert.assertEquals(excepted.getSentences(), actual.getSentences());
        Assert.assertEquals(excepted.getTitle(), actual.getTitle());
        Assert.assertEquals(excepted.getExtend(), actual.getExtend());
        AssertUtil.assertListEquals(excepted.getFileRefs(), actual.getFileRefs());
        AssertUtil.assertListEquals(excepted.getTags(), actual.getTags());
        Assert.assertEquals(excepted.getReview().getReputation(), excepted.getReview().getReputation());
        AssertUtil.assertListEquals(excepted.getReview().getReplies(), excepted.getReview().getReplies());
        return actual;
    }
}
