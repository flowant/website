package org.flowant.website;

import org.flowant.website.event.MockDataGenerateEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx =
                SpringApplication.run(BackendApplication.class, args);

        MockDataGenerateEvent.publishEventWhenActiveProfileIsTest(ctx);
    }
}
