package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Reply;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReplyRepository extends ReactiveCrudRepository<Reply, UUID> {
}
