package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class Reply {
    @NonNull
    String content;
    Reputation reputation = new Reputation();
    CRUDZonedTime crudTime = CRUDZonedTime.now();
}
