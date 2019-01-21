package org.flowant.website.model;

import org.flowant.website.util.test.UserMaker;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserTest {

    @Test
    public void testMaker() {
        log.debug("User:{}", UserMaker.small()::toString);
        log.debug("User:{}", UserMaker.large()::toString);
    }

}
