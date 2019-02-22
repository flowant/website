package org.flowant.website.model;

import java.io.Serializable;
import java.util.UUID;

import org.flowant.website.util.IdMaker;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@PrimaryKeyClass
public class IdCid implements HasIdentity, HasContainerId, Serializable {

    private static final long serialVersionUID = -264539125339801671L;

    @NonNull
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    UUID identity;

    @NonNull
    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    UUID containerId;

    public static IdCid of(String identity, String containerId) {
        return of(UUID.fromString(identity), UUID.fromString(containerId));
    }

    public static IdCid random() {
        return IdCid.of(IdMaker.randomUUID(), IdMaker.randomUUID());
    }

    public static IdCid random(UUID containerId) {
        return IdCid.of(IdMaker.randomUUID(), containerId);
    }

}
