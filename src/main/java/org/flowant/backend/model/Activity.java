package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data
@UserDefinedType
public class Activity {
    String action;
    CRUDZonedTime crudTime = CRUDZonedTime.now();
}
