package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType
public class Reputation {
    int rating;
    int liked;
    int reported;
    public static Reputation of(int rating, int liked, int reported) {
        return new Reputation(rating, liked, reported);
    }
}
