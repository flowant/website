package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Reply;
import org.springframework.data.cassandra.core.mapping.MapId;

public class ReplyMaker {

    static UUID replierId = IdMaker.randomUUID();
    static String replierName = "replierName";
    static String comment = "comment";

    public static Reply small(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        return Reply.of(id, IdMaker.toContainerId(mapId), replierId, replierName + id, CRUZonedTime.now());
    }

    public static Reply smallRandom() {
        return small(IdMaker.randomMapId());
    }

    public static Reply large(MapId mapId) {
        UUID id = IdMaker.toIdentity(mapId);
        Reply reply = small(mapId);
        reply.setComment(comment + id);
        return reply;
    }

    public static Reply largeRandom() {
        return large(IdMaker.randomMapId());
    }

}
