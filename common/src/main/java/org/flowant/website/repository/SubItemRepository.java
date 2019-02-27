package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.SubItem;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface SubItemRepository extends SubItemFragment<SubItem>, ReactiveCassandraRepository<SubItem, UUID> {

}
