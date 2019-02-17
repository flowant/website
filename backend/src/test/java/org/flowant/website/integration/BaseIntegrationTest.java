package org.flowant.website.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.HasId;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.User;
import org.flowant.website.rest.BaseRestTest;
import org.flowant.website.rest.ContentReputationRest;
import org.flowant.website.rest.ContentRest;
import org.flowant.website.rest.ReplyReputationRest;
import org.flowant.website.rest.ReplyRest;
import org.flowant.website.rest.ReviewReputationRest;
import org.flowant.website.rest.ReviewRest;
import org.flowant.website.rest.UserRest;
import org.junit.After;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class BaseIntegrationTest extends BaseRestTest {
    public final static String __ID__ = "{id}";

    @Data
    @RequiredArgsConstructor(staticName="of")
    static class ApiInfo<T extends HasId> {
        @NonNull
        String url;
        @NonNull
        Class<T> cls;
        List<HasId> deleteAfterTest = new ArrayList<>();
    }

    Map<String, ApiInfo<? extends HasId>> apiInfo = new HashMap<>();

    @Before
    public void before() {
        super.before();

        apiInfo.put(User.class.getSimpleName(), ApiInfo.of(UserRest.USER, User.class));
        apiInfo.put(Content.class.getSimpleName(), ApiInfo.of(ContentRest.CONTENT, Content.class));
        apiInfo.put(Review.class.getSimpleName(), ApiInfo.of(ReviewRest.REVIEW, Review.class));
        apiInfo.put(Reply.class.getSimpleName(), ApiInfo.of(ReplyRest.REPLY, Reply.class));
        apiInfo.put(ContentReputation.class.getSimpleName(), ApiInfo.of(
                ContentReputationRest.CONTENT_REPUTATION, ContentReputation.class));
        apiInfo.put(ReviewReputation.class.getSimpleName(), ApiInfo.of(
                ReviewReputationRest.REVIEW_REPUTATION, ReviewReputation.class));
        apiInfo.put(ReplyReputation.class.getSimpleName(), ApiInfo.of(
                ReplyReputationRest.REPLY_REPUTATION, ReplyReputation.class));
    }

    @After
    public void after() {
        apiInfo.forEach((name, info) -> {
            info.deleteAfterTest.forEach((HasId data) -> deleteById(data.getId(), info.cls));
        });
    }

    public <T extends HasId> T post(T data, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasId> info = apiInfo.get(classSimpleName);
        info.getDeleteAfterTest().add(data);

        T resp = WebClient.create().post().uri(uriBuilder -> uriBuilder.scheme(SCHEME)
                .host(host).port(port).path(info.getUrl()).build())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(data), cls)
                .exchange().block().bodyToMono(cls).block();
        log.trace("post:{}, return:{}", data, resp);
        return resp;
    }

    public <T extends HasId> T put(T data, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasId> info = apiInfo.get(classSimpleName);
        info.getDeleteAfterTest().add(data);

        T resp = WebClient.create().put().uri(uriBuilder -> uriBuilder.scheme(SCHEME)
                .host(host).port(port).path(info.getUrl()).build())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(data), cls)
                .exchange().block().bodyToMono(cls).block();
        log.trace("put:{}, return:{}", data, resp);
        return resp;
    }

    public <T> T getById(UUID id, Class<T> cls) {
        return getById(id.toString(), cls);
    }

    public <T> T getById(String id, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasId> info = apiInfo.get(classSimpleName);

        T resp = WebClient.create().get().uri(uriBuilder -> uriBuilder.scheme(SCHEME)
                .host(host).port(port).path(info.getUrl()).pathSegment(__ID__).build(id))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().block().bodyToMono(cls).block();
        log.trace("getById:{}, return:{}", id, resp);
        return resp;
    }

    public <T> void deleteById(UUID id, Class<T> cls) {
        deleteById(id.toString(), cls);
    }

    public <T> void deleteById(String id, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasId> info = apiInfo.get(classSimpleName);

        log.trace("deleteById:{}", id);
        webTestClient.delete().uri(uriBuilder -> uriBuilder.scheme(SCHEME)
                .host(host).port(port).path(info.getUrl()).pathSegment(__ID__).build(id))
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
