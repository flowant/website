package org.flowant.website.rest;

import java.net.URI;

import org.flowant.website.model.IdCid;
import org.springframework.web.util.UriBuilder;

public class UriUtil {
    public static <ID> URI getUri(ID id, UriBuilder builder) {
        builder.pathSegment("{id}");
        if (id instanceof IdCid) {
            builder.pathSegment("{cid}");
            IdCid idCid = IdCid.class.cast(id);
            return builder.build(idCid.getIdentity(), idCid.getContainerId());
        } else {
            return builder.build(id);
        }
    }
}
