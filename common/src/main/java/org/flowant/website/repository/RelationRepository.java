package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Relation;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface RelationRepository extends RelationFragment, ReactiveCassandraRepository<Relation, UUID> {

}
