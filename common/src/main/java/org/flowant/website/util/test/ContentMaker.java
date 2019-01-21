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
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
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

    public static Content small(int s) {
        return Content.of(UUID.randomUUID(), title + s, 
                Review.of(Reputation.of(rating + s, liked + s, reported + s)), CRUZonedTime.now());
    }

    public static Content small() {
        return small(0);
    }

    public static Content large(int s) {
        return Content.builder().id(UUID.randomUUID()).title(title + s)
                .extend(new Recipe(List.of(ingredients + s), prepareSeconds + s, cookSeconds + s,
                        servings + s, calory + s, nutritionFacts + s))
                .fileRefs(List.of(FileMaker.large()))
                .sentences(sentences + s)
                .tags(List.of(Tag.of(name + s)))
                .review(new Review(Reputation.of(rating + s, liked + s, reported + s),
                        List.of(new Reply(content + s, Reputation.of(rating + s, liked + s, reported + s),
                                CRUZonedTime.now()))))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static Content large() {
        return large(0);
    }

    @Test
    public void testMaker() {
        log.debug("Content:{}", small()::toString);
        log.debug("Content:{}", large()::toString);
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
