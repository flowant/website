package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Reply;

import com.datastax.driver.core.utils.UUIDs;

public class ReplyMaker {

    static UUID containerId = UUIDs.timeBased();
    static UUID replierId = UUIDs.timeBased();
    static String replierName = "replierName";
    static String comment = "comment";

    public static Reply small(UUID id) {
        return Reply.of(id, containerId, replierId, replierName + id, CRUZonedTime.now());
    }

    public static Reply smallRandom() {
        return small(UUIDs.timeBased());
    }

    public static Reply large(UUID id) {
        Reply reply = small(id);
        reply.setComment(comment + id);
        return reply;
    }

    public static Reply largeRandom() {
        return large(UUIDs.timeBased());
    }

}
