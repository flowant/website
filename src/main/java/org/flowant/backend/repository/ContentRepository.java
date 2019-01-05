package org.flowant.backend.repository;

import java.util.UUID;

import org.flowant.backend.model.Content;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContentRepository extends ReactiveCrudRepository<Content, UUID> {
    //TODO should delete multimedia when content is deleted
    //TODO should update multimediaRef when multimedia is uploaded.
}
