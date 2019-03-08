package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.IdScore;

import reactor.core.publisher.Mono;

public interface SubItemFragment {

    Mono<IdScore> addSubItem(UUID identity, IdScore idScore);

    Mono<IdScore> removeSubItem(UUID identity, IdScore idScore);

}
