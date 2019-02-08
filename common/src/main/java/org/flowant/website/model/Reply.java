package org.flowant.website.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Reply {
    @NonNull
    UUID replierId;
    @NonNull
    String replierName;
    String content;
    @NonNull
    Reputation reputation;
    @NonNull
    CRUZonedTime cruTime;
}
