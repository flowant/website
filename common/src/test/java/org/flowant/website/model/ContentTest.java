package org.flowant.website.model;

import org.flowant.website.util.test.AssertUtil;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ContentTest {

    @Test
    public void testMaker() {
        log.debug("Content:{}", ContentMaker.smallRandom()::toString);
        log.debug("Content:{}", ContentMaker.largeRandom()::toString);

        Content content = ContentMaker.largeRandom();
        AssertUtil.assertEquals(content, content);
    }

}
