package org.flowant.website.util.test;

import static java.lang.Integer.MAX_VALUE;

import java.util.Random;
import java.util.function.BiFunction;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Reputation;
import org.flowant.website.model.ReviewReputation;

public class ReputationMaker {

    static Random r = new Random();

    public static Reputation emptyReputation() {
        return new Reputation();
    }

    public static Reputation randomReputation() {
        return Reputation.of(r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE),
                r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE), r.nextInt(MAX_VALUE));
    }

    public static ContentReputation emptyContentReputation() {
        return ContentReputation.of(IdCid.random());
    }

    public static ContentReputation emptyContentReputation(IdCid idCid) {
        return ContentReputation.of(idCid);
    }

    public static ContentReputation randomContentReputation() {
        return randomContentReputation(IdCid.random());
    }

    public static ContentReputation randomContentReputation(IdCid idCid) {
        return ContentReputation.of(idCid, randomReputation());
    }

    public static ReviewReputation emptyReviewReputation() {
        return ReviewReputation.of(IdCid.random());
    }

    public static ReviewReputation emptyReviewReputation(IdCid idCid) {
        return ReviewReputation.of(idCid);
    }

    public static ReviewReputation randomReviewReputation() {
        return randomReviewReputation(IdCid.random());
    }

    public static ReviewReputation randomReviewReputation(IdCid idCid) {
        return ReviewReputation.of(idCid, randomReputation());
    }

    public static ReplyReputation emptyReplyReputation() {
        return ReplyReputation.of(IdCid.random());
    }

    public static ReplyReputation emptyReplyReputation(IdCid idCid) {
        return ReplyReputation.of(idCid);
    }

    public static ReplyReputation randomReplyReputation() {
        return randomReplyReputation(IdCid.random());
    }

    public static ReplyReputation randomReplyReputation(IdCid idCid) {
        return ReplyReputation.of(idCid, randomReputation());
    }

    public static <T> T emptyTypeReputation(BiFunction<IdCid, Reputation, T> supplier) {
        return supplier.apply(IdCid.random(), new Reputation());
    }

    public static <T> T emptyTypeReputation(IdCid idCid, BiFunction<IdCid, Reputation, T> supplier) {
        return supplier.apply(idCid, new Reputation());
    }

    public static <T> T randomTypeReputation(BiFunction<IdCid, Reputation, T> supplier) {
        return supplier.apply(IdCid.random(), randomReputation());
    }

    public static <T> T randomTypeReputation(IdCid idCid, BiFunction<IdCid, Reputation, T> supplier) {
        return supplier.apply(idCid, randomReputation());
    }

}
