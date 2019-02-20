package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasMapId extends HasIdentity, HasContainerId {

    public static final String IDENTITY = "identity";
    public static final String CONTAINER_ID = "containerId";

    @JsonIgnore
    default MapId getMapId() {
        return BasicMapId.id().with(IDENTITY, getIdentity()).with(CONTAINER_ID, getContainerId());
    }

    default HasMapId setMapId(MapId mapId) {
        UUID identity = (UUID) mapId.get(IDENTITY);
        if (identity != null) {
            setIdentity(identity);
        }
        UUID orderKey = (UUID) mapId.get(CONTAINER_ID);
        if (orderKey != null) {
            setContainerId(orderKey);
        }
        return this;
    }

}
