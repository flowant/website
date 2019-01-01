package org.flowant.backend.model;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class Review {
    @NonNull
    Reputation reputation;
    List<Reply> replies;
}
