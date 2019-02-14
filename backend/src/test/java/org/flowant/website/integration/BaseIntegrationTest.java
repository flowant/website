package org.flowant.website.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.HasId;
import org.flowant.website.model.User;
import org.flowant.website.rest.BaseRestTest;
import org.flowant.website.rest.ContentRest;
import org.flowant.website.rest.UserRest;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class BaseIntegrationTest extends BaseRestTest {
    public final static String __ID__ = "/{id}";

    @Value("${server.address}")
    private String address;

    @LocalServerPort
    private int port;

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

        address = "http://" + address + ":";
        apiInfo.put(Content.class.getSimpleName(), ApiInfo.of(address + port + ContentRest.CONTENT, Content.class));
        apiInfo.put(User.class.getSimpleName(), ApiInfo.of(address + port + UserRest.USER, User.class));
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

        T resp = WebClient.create().post().uri(info.getUrl()).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).body(Mono.just(data), cls)
                .exchange().block().bodyToMono(cls).block();
        log.trace("post:{}, return:{}", data, resp);
        return resp;
    }

    public <T> T getById(UUID id, Class<T> cls) {
        return getById(id.toString(), cls);
    }

    public <T> T getById(String id, Class<T> cls) {
        String classSimpleName = cls.getSimpleName();
        ApiInfo<? extends HasId> info = apiInfo.get(classSimpleName);

        T resp = WebClient.create().get().uri(info.getUrl() + __ID__, id)
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
        webTestClient.delete().uri(info.getUrl() + __ID__, id)
                .exchange()
                .expectStatus().isOk();
    }
}
