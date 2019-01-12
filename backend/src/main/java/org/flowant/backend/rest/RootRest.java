package org.flowant.backend.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class RootRest {
    final static String ROOT = "/";

    @Value("${server.address}")
    String serverAddress;
    @Value("${server.port}")
    int serverPort;

    @RequestMapping(ROOT)
    public Mono<String> welcome() {
        return Mono.just("backend:" + serverAddress + ":" + serverPort);
    }
}
