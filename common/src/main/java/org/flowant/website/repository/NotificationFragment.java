package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdCid;

import reactor.core.publisher.Mono;


public interface NotificationFragment {

    Mono<Boolean> removeSubscriber(IdCid idCid, UUID subscriber);

}
