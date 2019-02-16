package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Review;

public interface ReviewRepository extends PageableRepository<Review, UUID> {
}
