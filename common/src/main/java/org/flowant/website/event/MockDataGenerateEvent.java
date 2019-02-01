package org.flowant.website.event;

import org.springframework.context.ConfigurableApplicationContext;

public class MockDataGenerateEvent {
    public static void publishEventWhenActiveProfileIsTest(ConfigurableApplicationContext ctx) {
        for (String profile : ctx.getEnvironment().getActiveProfiles()) {
            if ("test".equalsIgnoreCase(profile)) {
                ctx.publishEvent(new MockDataGenerateEvent());
            }
        }
    }
}
