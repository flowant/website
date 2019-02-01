package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Activity {
    @NonNull
    String action;
    @NonNull
    CRUZonedTime cruTime;
}
