package org.flowant.website.util.test;

import static java.lang.Integer.MAX_VALUE;

import java.util.Random;
import java.util.function.BiFunction;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.ReviewReputation;
import org.springframework.data.cassandra.core.mapping.MapId;

public class ReputationMaker {
    static Random r = new Random();

    public static Reputation randomReputation() {
        return Reputation.of(r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE),
                r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE));
    }

    public static ContentReputation randomContentReputation() {
        return randomContentReputation(IdMaker.randomMapId());
    }

    public static ContentReputation randomContentReputation(MapId mapId) {
        return ContentReputation.of(mapId, randomReputation());
    }

    public static ReviewReputation randomReviewReputation() {
        return randomReviewReputation(IdMaker.randomMapId());
    }

    public static ReviewReputation randomReviewReputation(MapId mapId) {
        return ReviewReputation.of(mapId, randomReputation());
    }

    public static ReplyReputation randomReplyReputation() {
        return randomReplyReputation(IdMaker.randomMapId());
    }

    public static ReplyReputation randomReplyReputation(MapId mapId) {
        return ReplyReputation.of(mapId, randomReputation());
    }

    public static <T> T emptyTypeReputation(BiFunction<MapId, Reputation, T> supplier) {
        return supplier.apply(IdMaker.randomMapId(), new Reputation());
    }

    public static <T> T emptyTypeReputation(MapId id, BiFunction<MapId, Reputation, T> supplier) {
        return supplier.apply(id, new Reputation());
    }

    public static <T> T randomTypeReputation(BiFunction<MapId, Reputation, T> supplier) {
        return supplier.apply(IdMaker.randomMapId(), randomReputation());
    }

    public static <T> T randomTypeReputation(MapId id, BiFunction<MapId, Reputation, T> supplier) {
        return supplier.apply(id, randomReputation());
    }

}
