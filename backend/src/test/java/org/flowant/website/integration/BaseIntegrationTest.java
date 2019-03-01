package org.flowant.website.integration;

import static org.flowant.website.rest.PageableRepositoryRest.CID;
import static org.flowant.website.rest.PageableRepositoryRest.PAGE;
import static org.flowant.website.rest.PageableRepositoryRest.SIZE;
import static org.flowant.website.rest.UriUtil.getUri;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.HasIdentity;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.SubItem;
import org.flowant.website.model.User;
import org.flowant.website.model.WebSite;
import org.flowant.website.rest.ContentReputationRest;
import org.flowant.website.rest.ContentRest;
import org.flowant.website.rest.LinkUtil;
import org.flowant.website.rest.SubItemRest;
import org.flowant.website.rest.ReplyReputationRest;
import org.flowant.website.rest.ReplyRest;
import org.flowant.website.rest.RestTest;
import org.flowant.website.rest.ReviewReputationRest;
import org.flowant.website.rest.ReviewRest;
import org.flowant.website.rest.UserRest;
import org.flowant.website.rest.WebSiteRest;
import org.junit.After;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
public abstract class BaseIntegrationTest extends RestTest {
    public final static String __ID__ = "{id}";

    @Data
    @RequiredArgsConstructor(staticName = "of")
    static class ApiInfo<T extends HasIdentity> {
        @NonNull
        String url;
        @NonNull
        Class<T> cls;
        List<HasIdentity> deleteAfterTest = new ArrayList<>();
    }

    Map<String, ApiInfo<? extends HasIdentity>> apiInfo = new HashMap<>();

    @Before
    public void before() {
        super.before();

        apiInfo.put(WebSite.class.getSimpleName(), ApiInfo.of(WebSiteRest.WEBSITE, WebSite.class));
        apiInfo.put(SubItem.class.getSimpleName(), ApiInfo.of(SubItemRest.SUBITEM, SubItem.class));
        apiInfo.put(User.class.getSimpleName(), ApiInfo.of(UserRest.USER, User.class));
        apiInfo.put(Content.class.getSimpleName(), ApiInfo.of(ContentRest.CONTENT, Content.class));
        apiInfo.put(Review.class.getSimpleName(), ApiInfo.of(ReviewRest.REVIEW, Review.class));
        apiInfo.put(Reply.class.getSimpleName(), ApiInfo.of(ReplyRest.REPLY, Reply.class));
        apiInfo.put(ContentReputation.class.getSimpleName(),
                ApiInfo.of(ContentReputationRest.CONTENT_REPUTATION, ContentReputation.class));
        apiInfo.put(ReviewReputation.class.getSimpleName(),
                ApiInfo.of(ReviewReputationRest.REVIEW_REPUTATION, ReviewReputation.class));
        apiInfo.put(ReplyReputation.class.getSimpleName(),
                ApiInfo.of(ReplyReputationRest.REPLY_REPUTATION, ReplyReputation.class));
    }

    @After
    public void after() {
        apiInfo.forEach((name, info) -> {
            info.deleteAfterTest.forEach((HasIdentity data) -> {
                if (data instanceof HasIdCid) {
                    deleteById(HasIdCid.class.cast(data).getIdCid(), info.cls);
                } else {
                    deleteById(data.getIdentity(), info.cls);
                }
            });
        });
    }

    public <T extends HasIdentity> T post(T data, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasIdentity> info = apiInfo.get(classSimpleName);
        info.getDeleteAfterTest().add(data);

        T resp = WebClient.create().post()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(info.getUrl()).build())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(data), cls)
                .exchange().block()
                .bodyToMono(cls).block();

        log.trace("post:{}, return:{}", data, resp);
        return resp;
    }

    public <T extends HasIdentity> T put(T data, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasIdentity> info = apiInfo.get(classSimpleName);
        info.getDeleteAfterTest().add(data);

        T resp = WebClient.create().put()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(info.getUrl()).build())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(data), cls)
                .exchange().block()
                .bodyToMono(cls).block();

        log.trace("put:{}, return:{}", data, resp);
        return resp;
    }

    public <T, ID> T getById(ID id, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasIdentity> info = apiInfo.get(classSimpleName);

        T resp = WebClient.create().get()
                .uri(uriBuilder -> getUri(id, uriBuilder.scheme(SCHEME).host(host).port(port).path(info.getUrl())))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().block()
                .bodyToMono(cls).block();

        log.trace("getById:{}, return:{}", id, resp);
        return resp;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor(staticName = "of")
    public static class EntitiesAndNextLink<T> {
        Flux<T> entities;
        Optional<URI> next;
    }

    public <T> EntitiesAndNextLink<T> getPageByContainerId(UUID containerId, Class<T> cls, int pageSize) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasIdentity> info = apiInfo.get(classSimpleName);

        ClientResponse resp = WebClient.create().get()
                .uri(uriBuilder -> uriBuilder.scheme(SCHEME).host(host).port(port).path(info.getUrl())
                        .queryParam(CID, containerId.toString()).queryParam(PAGE, "0")
                        .queryParam(SIZE, String.valueOf(pageSize)).build())
                .exchange().block();

        return EntitiesAndNextLink.of(resp.bodyToFlux(cls), LinkUtil.getNextUrl(resp.headers().asHttpHeaders()));
    }

    public <T, ID> void deleteById(ID id, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasIdentity> info = apiInfo.get(classSimpleName);

        log.trace("deleteById:{}", id);
        webTestClient.delete()
                .uri(uriBuilder -> getUri(id, uriBuilder.scheme(SCHEME).host(host).port(port).path(info.getUrl())))
                .exchange()
                .expectStatus().isOk();
    }

    public Consumer<Content> postContent = c -> post(c, Content.class);
    public Consumer<ContentReputation> postContentReputation = cr -> post(cr, ContentReputation.class);
    public Consumer<User> postUser = u -> post(u, User.class);
    public Consumer<Review> postReview = rv -> post(rv, Review.class);
    public Consumer<ReviewReputation> postReviewReputation = rvr -> post(rvr, ReviewReputation.class);
    public Consumer<Reply> postReply = rp -> post(rp, Reply.class);
    public Consumer<ReplyReputation> postReplyReputation = rpr -> post(rpr, ReplyReputation.class);
}
