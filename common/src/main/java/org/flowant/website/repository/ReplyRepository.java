package org.flowant.website.repository;

import org.flowant.website.model.Reply;
import org.springframework.data.cassandra.core.mapping.MapId;

public interface ReplyRepository extends PageableRepository<Reply, MapId> {
}
