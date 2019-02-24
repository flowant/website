package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Reply;
import org.flowant.website.util.IdMaker;

public class ReplyMaker {

    static UUID replierId = IdMaker.randomUUID();
    static String replierName = "replierName";
    static String comment = "comment";

    public static Reply small(IdCid idCid) {
        UUID id = idCid.getIdentity();
        return Reply.of(idCid, replierId, replierName + id, ReputationMaker.emptyReputation(), CRUZonedTime.now());
    }

    public static Reply smallRandom(UUID containerId) {
        return small(IdCid.random(containerId));
    }

    public static Reply smallRandom() {
        return small(IdCid.random());
    }

    public static Reply large(IdCid idCid) {
        UUID id = idCid.getIdentity();
        Reply reply = small(idCid);
        reply.setComment(comment + id);
        return reply;
    }

    public static Reply largeRandom(UUID containerId) {
        return large(IdCid.random(containerId));
    }

    public static Reply largeRandom() {
        return large(IdCid.random());
    }

}
