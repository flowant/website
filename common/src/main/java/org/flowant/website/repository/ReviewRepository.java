package org.flowant.website.repository;

import org.flowant.website.model.Review;
import org.springframework.data.cassandra.core.mapping.MapId;

public interface ReviewRepository extends PageableRepository<Review, MapId> {
}
