package org.flowant.website.repository;

import org.flowant.website.model.Content;
import org.springframework.data.cassandra.core.mapping.MapId;

public interface ContentRepository extends PageableRepository<Content, MapId> {
}
