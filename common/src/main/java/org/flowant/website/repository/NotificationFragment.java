package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Notification;

import reactor.core.publisher.Mono;


public interface NotificationFragment {

    Mono<Notification> saveWithTtl(Notification notification, long ttlMillis);

    Mono<UUID> removeSubscriber(IdCid idCid, UUID subscriber);

}
