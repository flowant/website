package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.model.HasMapId;
import org.flowant.website.repository.MapIdRepository;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;

public abstract class MapIdRepositoryRest <T extends HasMapId, R extends MapIdRepository<T>>
        extends RepositoryRest<T, MapId, R> {

    public final static String CID = "cid";
    public final static String PATH_SEG_ID_CID = "/{id}/{cid}";

    public static MapId toMapId(String identity, String containerId) {
        return BasicMapId.id()
                .with(HasMapId.IDENTITY, UUID.fromString(identity))
                .with(HasMapId.CONTAINER_ID, UUID.fromString(containerId));
    }

}
