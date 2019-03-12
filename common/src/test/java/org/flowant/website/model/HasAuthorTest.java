package org.flowant.website.model;

import static org.junit.Assert.assertEquals;

import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;

public class HasAuthorTest {

    @Test
    public void testSetAuthor() {

        User user = UserMaker.largeRandom();
        Review review = ReviewMaker.largeRandom().setAuthor(user);
        assertEquals(review.getAuthorId(), user.getIdentity());

        Reply reply = ReplyMaker.largeRandom().setAuthor(review);
        assertEquals(review.getAuthorId(), reply.getAuthorId());
        assertEquals(review.getAuthorName(), reply.getAuthorName());
    }

}
