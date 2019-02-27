package org.flowant.website.model;

import java.util.Random;
import java.util.UUID;

import org.flowant.website.util.IdMaker;
import org.springframework.data.cassandra.core.mapping.Element;
import org.springframework.data.cassandra.core.mapping.Tuple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@Tuple
public class IdScore {

    @Element(0)
    UUID identity;

    @Element(1)
    long score;

    public static IdScore random() {
        return random(IdMaker.randomUUID());
    }

    public static IdScore random(UUID identity) {
        Random r = new Random();
        return IdScore.of(identity, r.nextInt(Integer.MAX_VALUE));
    }

}
