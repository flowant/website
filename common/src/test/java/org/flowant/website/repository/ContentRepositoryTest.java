package org.flowant.website.repository;

import org.flowant.website.model.Content;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentRepositoryTest extends BaseRepositoryTest<Content, ContentRepository> {

    @Test
    public void crud() {
        testCrud(ContentMaker::smallRandom, ContentMaker::largeRandom);
    }
}
