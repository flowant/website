package org.flowant.website.util;

import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;

public class IdMaker {
    public static UUID randomUUID() {
        return UUIDs.timeBased();
    }
}
