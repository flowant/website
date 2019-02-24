package org.flowant.website.repository;

import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;
import org.springframework.util.ClassUtils;

public class CassandraTemplateUtil {

    public static CqlIdentifier getTableName(ReactiveCassandraOperations operations, Class<?> entityClass) {
        return operations
                .getConverter()
                .getMappingContext()
                .getRequiredPersistentEntity(ClassUtils.getUserClass(entityClass))
                .getTableName();
    }

}
