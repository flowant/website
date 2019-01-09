package org.flowant.backend.model;

import java.util.List;

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
public class Review {
    @NonNull
    Reputation reputation;
    List<Reply> replies;
}
