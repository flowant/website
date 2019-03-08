package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Message;
import org.flowant.website.util.IdMaker;

public class MessageMaker {

    static UUID senderId = IdMaker.randomUUID();
    static String senderName = "senderName";
    static String sentences = "sentences";

    public static Message random(IdCid idCid) {
        UUID id = idCid.getIdentity();
        return Message.of(idCid, senderId, senderName + id, sentences);
    }

    public static Message random(UUID containerId) {
        return random(IdCid.random(containerId));
    }

    public static Message random() {
        return random(IdCid.random());
    }

}
