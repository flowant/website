package org.flowant.website.rest;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

import org.flowant.website.model.Authority;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class RestTest {

    public final static String SCHEME = "http";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    protected ApplicationContext context;

    protected WebTestClient webTestClient;

    protected String host = "localhost";

    @LocalServerPort
    protected int port;

    @Before
    public void before() {
        webTestClient = WebTestClient
            .bindToApplicationContext(context)
            .apply(springSecurity())
            .configureClient()
            .build().mutateWith(csrf())
            .mutateWith(mockUser().authorities(Authority.of(Authority.USER)));
    }

}
