package org.flowant.website.repository;

import java.util.HashMap;

import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.HasMapId;
import org.flowant.website.model.HasReputation;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.util.test.IdMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class RelationshipService {

    static ContentRepository repoContent;
    static ReviewRepository repoReview;
    static ReplyRepository repoReply;
    static ContentReputationRepository repoContentRpt;
    static ReviewReputationRepository repoReviewRpt;
    static ReplyReputationRepository repoReplyRpt;

    static HashMap<Class<?>, PageableRepository<? extends HasReputation>> repoParent = new HashMap<>();
    static Flux<MapIdRepository<?>> repoChildren;

    @Autowired
    RelationshipService(@Qualifier("contentRepository") ContentRepository repoContent,
            @Qualifier("reviewRepository") ReviewRepository repoReview,
            @Qualifier("replyRepository") ReplyRepository repoReply,
            @Qualifier("contentReputationRepository") ContentReputationRepository repoContentRpt,
            @Qualifier("reviewReputationRepository") ReviewReputationRepository repoReviewRpt,
            @Qualifier("replyReputationRepository") ReplyReputationRepository repoReplyRpt) {

        RelationshipService.repoContent = repoContent;
        RelationshipService.repoReview = repoReview;
        RelationshipService.repoReply = repoReply;
        RelationshipService.repoContentRpt = repoContentRpt;
        RelationshipService.repoReviewRpt = repoReviewRpt;
        RelationshipService.repoReplyRpt = repoReplyRpt;

        repoParent.put(ContentReputation.class, repoContent);
        repoParent.put(ReviewReputation.class, repoReview);
        repoParent.put(ReplyReputation.class, repoReply);

        repoChildren = Flux.just(repoReview, repoReply, repoContentRpt, repoReviewRpt, repoReplyRpt).cache();
    }

    // delete content and all children
    public static Mono<Void> deleteChildren(MapId mapId) {
        return repoChildren.flatMap(repo -> repo.deleteAllByContainerId(IdMaker.toContainerId(mapId))).then();
    }

    // Update counter values or create row if not exist from XReputationRepository to XRepository.
    public static <T extends ReputationCounter> Mono<T> updateReputation(T reputationCounter) {
        PageableRepository<?> repo = repoParent.get(reputationCounter.getClass());
        return repo.updateReputationById(reputationCounter.getMapId(), reputationCounter.toReputation())
                .thenReturn(reputationCounter);
    }

    // Used to check results of updateReputation method in test cases.
    public static <T extends ReputationCounter> Mono<? extends HasReputation> findReputation(T reputationCounter) {
        PageableRepository<? extends HasMapId> repo = repoParent.get(reputationCounter.getClass());
        return repo.findById(reputationCounter.getMapId());
    }

    // Used for cleaning up test data generated by test cases.
    public static <T extends HasMapId> Mono<Void> deleteParent(T entity) {
        PageableRepository<?> repo = repoParent.get(entity.getClass());
        return repo.deleteById(entity.getMapId());
    }

}
