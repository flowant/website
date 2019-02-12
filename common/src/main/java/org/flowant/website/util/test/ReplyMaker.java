package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Reply;

public class ReplyMaker {

    static UUID containerId = UUID.randomUUID();
    static UUID replierId = UUID.randomUUID();
    static String replierName = "replierName";
    static String comment = "comment";

    public static Reply small(UUID id) {
        return Reply.of(id, containerId, replierId, replierName + id, CRUZonedTime.now());
    }

    public static Reply smallRandom() {
        return small(UUID.randomUUID());
    }

    public static Reply large(UUID id) {
        Reply reply = small(id);
        reply.setComment(comment + id);
        return reply;
    }

    public static Reply largeRandom() {
        return large(UUID.randomUUID());
    }

}
