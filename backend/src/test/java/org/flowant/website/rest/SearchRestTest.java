package org.flowant.website.rest;

import static org.flowant.website.rest.PageableRepositoryRest.PAGE;
import static org.flowant.website.rest.PageableRepositoryRest.SIZE;
import static org.flowant.website.rest.SearchRest.SEARCH;
import static org.flowant.website.rest.SearchRest.TAG;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ContentMaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
@Log4j2
public class SearchRestTest extends RestWithRepositoryTest<Content, IdCid, ContentRepository> {

    @Test
    public void searchTagPagination() {
        int cntEntities = 10;
        int pageSize = 3;

        String tag = IdMaker.randomUUID().toString();

        Flux<Content> contents = Flux.range(1, cntEntities).map(i -> ContentMaker.largeRandom().setTags(Set.of(tag))).cache();
        repo.saveAll(contents).blockLast();
        cleaner.registerToBeDeleted(contents);

        ClientResponse resp = WebClient.create().get()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(SEARCH)
                        .queryParam(TAG, tag)
                        .queryParam(PAGE, "0")
                        .queryParam(SIZE, String.valueOf(pageSize)).build())
                .exchange().block();

        List<Content> list = new ArrayList<>();
        Optional<URI> nextUrl;

        while(true) {
            list.addAll(resp.bodyToFlux(Content.class).collectList().block());
            nextUrl = LinkUtil.getNextUrl(resp.headers().asHttpHeaders());
            if (nextUrl.isEmpty()) {
                break;
            }
            log.trace("nextUrl:{}", nextUrl);
            resp = WebClient.create().get().uri(nextUrl.get()).exchange().block();
        }
        Assert.assertTrue(contents.all(c -> list.contains(c)).block());
    }

}
