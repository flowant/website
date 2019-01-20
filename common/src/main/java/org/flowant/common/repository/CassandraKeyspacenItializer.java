package org.flowant.common.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@ToString
@Log4j2
public class CassandraKeyspacenItializer {
    @Value("${spring.data.cassandra.contactPoints}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.cql-create-keyspace}")
    private String cqlCreateKeySpace;

    @PostConstruct
    public void createKeySpaceIfNotExist() {
        String cql = String.format(cqlCreateKeySpace, keyspaceName);
        try (Session session = Cluster.builder().addContactPoint(contactPoints).withPort(port).build().newSession()) {
            session.execute(cql);
        }
        log.debug("CassandraKeyspacenItializer:{}, performed cql: {}", this::toString, () -> cql);
    }
}