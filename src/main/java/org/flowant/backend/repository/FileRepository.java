package org.flowant.backend.repository;

import java.util.UUID;

import org.flowant.backend.model.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File, UUID> {
}
