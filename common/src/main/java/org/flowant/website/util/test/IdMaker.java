package org.flowant.website.util.test;

import static org.flowant.website.model.HasMapId.CONTAINER_ID;
import static org.flowant.website.model.HasMapId.IDENTITY;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import com.datastax.driver.core.utils.UUIDs;

public class IdMaker {

    public static UUID randomUUID() {
        return UUIDs.timeBased();
    }

    public static MapId randomMapId() {
        return BasicMapId.id().with(IDENTITY, UUIDs.timeBased()).with(CONTAINER_ID, UUIDs.timeBased());
    }

    public static UUID toIdentity(MapId mapId) {
        return (UUID) mapId.get(IDENTITY);
    }

    public static UUID toContainerId(MapId mapId) {
        return (UUID) mapId.get(CONTAINER_ID);
    }

}
