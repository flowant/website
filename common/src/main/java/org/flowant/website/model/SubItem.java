package org.flowant.website.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class SubItem implements HasIdentity {

    @NonNull
    @Id
    @Column("id")
    UUID identity;

    @NonNull
    @Column("si")
    List<IdScore> subItems;

    public static SubItem of(UUID indentity) {
        return of(indentity, new ArrayList<IdScore>());
    }

    public Flux<IdCid> toIdCids() {
        return Flux.fromIterable(subItems).map(idScore -> IdCid.of(idScore.getIdentity(), identity));
    }

}
