package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Activity {

    @NonNull
    String action;

    @NonNull
    CRUZonedTime cruTime;

}
