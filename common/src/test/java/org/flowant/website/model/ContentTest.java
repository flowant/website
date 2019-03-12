package org.flowant.website.model;

import static org.junit.Assert.assertEquals;

import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ContentTest {

    @Test
    public void testMaker() {
        log.debug("Content:{}", ContentMaker.smallRandom()::toString);
        log.debug("Content:{}", ContentMaker.largeRandom()::toString);

        Content content = ContentMaker.largeRandom();
        assertEquals(content, content);
    }

    @Test
    public void testSetAuthor() {
        log.debug("Content:{}", ContentMaker.smallRandom()::toString);

        User user = UserMaker.largeRandom();
        Content content = ContentMaker.largeRandom().setAuthor(user);
        assertEquals(content.getAuthorId(), user.getIdentity());
    }

}
