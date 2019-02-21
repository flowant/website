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
public class Reputation implements Comparable<Reputation> {
    long viewed;
    long rated;
    long liked;
    long disliked;
    long reported;
    long reputed;

    public static Reputation of(ReputationCounter r) {
        return of(r.getViewed(), r.getRated(), r.getLiked(),
                r.getDisliked(), r.getReported(), r.getReputed());
    }

    public static Reputation of(Reputation first, Reputation second) {
        return of(first.viewed + second.viewed, first.rated + second.rated, first.liked + second.liked,
                first.disliked + second.disliked, first.reported + second.reported, first.reputed + second.reputed);
    }

    @Override
    public int compareTo(Reputation reputation) {
        return Long.compare(this.getLiked(), reputation.getLiked());
    }

}
