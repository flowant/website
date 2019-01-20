package org.flowant.common.model;

import org.flowant.common.util.test.FileMaker;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileTest {

    @Test
    public void testMaker() {
        log.debug("Multimedia:{}", FileMaker.large()::toString);
    }

}
