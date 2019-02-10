package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReviewRepository extends ReactiveCrudRepository<Review, UUID> {
}
