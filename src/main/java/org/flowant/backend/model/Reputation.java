package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@UserDefinedType
public class Reputation {
    int rating;
    int liked;
    int reported;
}
