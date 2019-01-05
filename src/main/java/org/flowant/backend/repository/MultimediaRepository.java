package org.flowant.backend.repository;

import java.util.UUID;

import org.flowant.backend.model.Multimedia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MultimediaRepository extends ReactiveCrudRepository<Multimedia, UUID> {
}
