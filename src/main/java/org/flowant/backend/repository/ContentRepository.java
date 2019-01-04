package org.flowant.backend.repository;

import java.util.UUID;

import org.flowant.backend.model.Content;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContentRepository extends ReactiveCrudRepository<Content, UUID> {
}
