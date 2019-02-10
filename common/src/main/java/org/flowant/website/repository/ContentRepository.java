package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Content;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ContentRepository extends ReactiveCrudRepository<Content, UUID> {
}
