package org.flowant.website.util.test;

import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.Category;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;
import org.flowant.website.util.IdMaker;

public class NotificationMaker {

    static Set<UUID> subscribers = Set.of(IdMaker.randomUUID());
    static String authorName = "authorName";
    static Category category = Category.L;
    static UUID referenceId = IdMaker.randomUUID();
    static UUID referenceCid = IdMaker.randomUUID();
    static String appendix = "appendix";

    public static Notification small(IdCid idCid) {
        return Notification.of(idCid, authorName, subscribers, category);
    }

    public static Notification smallRandom(UUID containerId) {
        return small(IdCid.random(containerId));
    }

    public static Notification smallRandom() {
        return small(IdCid.random());
    }

    public static Notification large(IdCid idCid) {
        UUID id = idCid.getIdentity();
        Notification notification = small(idCid);
        notification.setReferenceId(referenceId);
        notification.setReferenceCid(referenceCid);
        notification.setAppendix(appendix + id);
        return notification;
    }

    public static Notification largeRandom(UUID containerId) {
        return large(IdCid.random(containerId));
    }

    public static Notification largeRandom() {
        return large(IdCid.random());
    }

}
