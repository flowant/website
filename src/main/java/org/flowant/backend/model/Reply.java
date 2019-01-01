package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class Reply {
    String content;
    @NonNull
    Reputation reputation;
    @NonNull
    CRUDZonedTime crudTime;
}
