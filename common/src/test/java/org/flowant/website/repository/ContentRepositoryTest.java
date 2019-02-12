package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentRepositoryTest extends BaseRepositoryTest<Content, UUID, ContentRepository> {

    @Test
    public void crud() {
        testCrud(Content::getId, ContentMaker::smallRandom, ContentMaker::largeRandom);
    }
}
