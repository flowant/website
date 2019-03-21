package org.flowant.website.rest;

import static org.flowant.website.rest.SearchRest.TAG;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
public class SearchRestTest extends RestWithRepositoryTest<Content, IdCid, ContentRepository> {

    @Before
    public void before() {
        super.before();

        setTestParams(SearchRest.PATH_SEARCH, Content.class, Content::getIdCid,
                ContentMaker::smallRandom, ContentMaker::largeRandom,
                c -> c.setTitle("newTitle"));
    }

    @Test
    public void searchTagPagination() {
        Function<UUID, Content> supplier = (tag) -> ContentMaker.largeRandom().setTags(Set.of(tag.toString()));

        pagination(10, 1, TAG, supplier);
        pagination(10, 3, TAG, supplier);
        pagination(10, 5, TAG, supplier);
        pagination(10, 11, TAG, supplier);
    }

}
