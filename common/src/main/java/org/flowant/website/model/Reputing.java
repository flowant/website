package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Reputing {
    long rating;
    long liking;
    long disliking;
    long reporting;

    public static Reputing of(Reputing first, Reputing second) {
        return of(first.rating + second.rating, first.liking + second.liking,
                first.disliking + second.disliking, first.reporting + second.reporting);
    }
}
