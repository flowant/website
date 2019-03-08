package org.flowant.website.repository;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface RelationFragment {

    Mono<UUID> follow(UUID identity, UUID followee);

    Mono<UUID> unfollow(UUID identity, UUID followee);

}
