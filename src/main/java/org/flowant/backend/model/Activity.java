package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class Activity {
    @NonNull
    String action;
    @NonNull
    CRUDZonedTime crudTime;
}
