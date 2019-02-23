package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.WebSite;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface WebSiteRepository extends ReactiveCassandraRepository<WebSite, UUID> {

}
