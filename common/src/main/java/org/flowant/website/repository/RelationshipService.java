package org.flowant.website.repository;

import static org.flowant.website.WebSiteConfig.getMaxSubItems;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.HasReputation;
import org.flowant.website.model.IdCid;
import org.flowant.website.model.IdScore;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.ReputationCounter;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.SubItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class RelationshipService {

    static WebSiteRepository repoWebSite;
    static ContentRepository repoContent;
    static ReviewRepository repoReview;
    static ReplyRepository repoReply;
    static ContentReputationRepository repoContentRpt;
    static ReviewReputationRepository repoReviewRpt;
    static ReplyReputationRepository repoReplyRpt;
    static SubItemRepository repoSubItem;
    static MessageRepository repoMessage;
    static NotificationRepository repoNotification;
    static RelationRepository repoRelation;

    static HashMap<Class<?>, ReputationRepository<? extends HasReputation>> repoReputation = new HashMap<>();

    static HashMap<Class<?>, ReputationCounterRepository<? extends HasIdCid>> repoCounter = new HashMap<>();

    @Autowired
    RelationshipService(ContentRepository repoContent,
                        ReviewRepository repoReview,
                        ReplyRepository repoReply,
                        ContentReputationRepository repoContentRpt,
                        ReviewReputationRepository repoReviewRpt,
                        ReplyReputationRepository repoReplyRpt,
                        SubItemRepository repoSubItem,
                        MessageRepository repoMessage,
                        NotificationRepository repoNotification,
                        RelationRepository repoRelation) {

        RelationshipService.repoContent = repoContent;
        RelationshipService.repoReview = repoReview;
        RelationshipService.repoReply = repoReply;
        RelationshipService.repoContentRpt = repoContentRpt;
        RelationshipService.repoReviewRpt = repoReviewRpt;
        RelationshipService.repoReplyRpt = repoReplyRpt;
        RelationshipService.repoSubItem = repoSubItem;
        RelationshipService.repoMessage = repoMessage;
        RelationshipService.repoNotification = repoNotification;
        RelationshipService.repoRelation = repoRelation;

        repoReputation.put(ContentReputation.class, repoContent);
        repoReputation.put(ReviewReputation.class, repoReview);
        repoReputation.put(ReplyReputation.class, repoReply);

        repoCounter.put(Content.class, repoContentRpt);
        repoCounter.put(Review.class, repoReviewRpt);
        repoCounter.put(Reply.class, repoReplyRpt);

    }

    public static Mono<Void> deleteUserRelationship(UUID identity) {
        return repoMessage.deleteAllByIdCidContainerId(identity)
                .then(repoNotification.deleteAllByIdCidContainerId(identity))
                .then(repoRelation.deleteById(identity));
    }

    public static <T extends HasIdCid> Mono<Void> deleteReviewsByContainerId(UUID containerId) {
        return repoReview.findAllByIdCidContainerId(containerId)
                .flatMap(review -> repoReview.deleteByIdWithRelationship(review.getIdCid()))
                .then();
    }

    public static <T extends HasIdCid> Mono<Void> deleteReputationByContainerId(UUID containerId, Class<T> counterCls) {
        ReputationRepository<?> repo = repoReputation.get(counterCls);
        return repo.deleteAllByIdCidContainerId(containerId).then();
    }

    public static <T extends HasIdCid> Mono<Void> deleteCounterByContainerId(UUID containerId, Class<T> reputationCls) {
        ReputationCounterRepository<?> repo = repoCounter.get(reputationCls);
        return repo.deleteAllByIdCidContainerId(containerId).then();
    }

    public static <T extends HasIdCid> Mono<Void> deleteCounter(IdCid idCid, Class<T> reputationCls) {
        ReputationCounterRepository<?> repo = repoCounter.get(reputationCls);
        return repo.deleteById(idCid);
    }

    public static Mono<Void> deleteSubItemById(UUID identity) {
        return repoSubItem.deleteById(identity);
    }

    // Update counter values or create row if not exist from XReputationRepository to XRepository.
    public static <T extends ReputationCounter> Mono<T> updateReputation(T reputationCounter) {
        ReputationRepository<?> repo = repoReputation.get(reputationCounter.getClass());
        return repo.updateReputationById(reputationCounter.getIdCid(), reputationCounter.toReputation())
                .thenReturn(reputationCounter);
    }

    // Used to check results of updateReputation method in test cases.
    public static <T extends ReputationCounter> Mono<? extends HasReputation> findReputation(T reputationCounter) {
        ReputationRepository<? extends HasIdCid> repo = repoReputation.get(reputationCounter.getClass());
        return repo.findById(reputationCounter.getIdCid());
    }

    // Used for cleaning up test data generated by test cases.
    public static <T extends HasIdCid> Mono<Void> deleteReputation(T reputationCounter) {
        ReputationRepository<?> repo = repoReputation.get(reputationCounter.getClass());
        return repo.deleteById(reputationCounter.getIdCid());
    }

    // Be careful, it's very heavy IO bound function.
    public static Mono<Collection<ReputationCounter>> findPopularSubItems(int cntPopular, UUID containerId, Class<?> reputationCls) {

        // prevent out of memory
        final int MAX_HEAP_SIZE = 1000;

        log.warn("Be careful, it's very heavy IO bound function. do not use in production.");

        ReputationCounterRepository<?> repo = repoCounter.get(reputationCls);

        int maxSize = Math.min(cntPopular + 1, MAX_HEAP_SIZE);

        PriorityQueue<ReputationCounter> minHeap = new PriorityQueue<>(maxSize + 1,
                Comparator.comparing(ReputationCounter::getLiked));

        // TODO restrict amount of IO
        return repo.findAllByIdCidContainerId(containerId)
                .doOnNext(r -> {
                    minHeap.add(r); // log n
                    // This way is faster than using MinMaxPriorityQueue with max size although remove() is used.
                    if (minHeap.size() == maxSize) {
                        minHeap.remove(); // log n
                    }
                }).then(Mono.just(minHeap));
    }

    public static Mono<SubItem> updatePopularSubItems(int maxSize, SubItem subItem, IdScore candidate) {

        log.trace("updatePopularSubItems, subItem :{}, subItems.size:{}, candidate:{}",
                () -> subItem, subItem.getSubItems()::size, () -> candidate);

        List<IdScore> idScores = subItem.getSubItems();

        if (idScores.size() < maxSize) {
            log.trace("updatePopularSubItems, addAndReturn");
            return repoSubItem.addSubItem(subItem.getIdentity(), candidate)
                    .thenReturn(subItem);
        }

        idScores.add(candidate);
        idScores.sort(Comparator.comparing(IdScore::getScore).reversed());
        List<IdScore> toBeDeleted = idScores.subList(maxSize, idScores.size());

        Mono<IdScore> addCandidate = Mono.empty();

        if (toBeDeleted.remove(candidate)) {
            log.trace("updatePopularSubItems, candidate is dropped");
        } else {
            log.trace("updatePopularSubItems, candidate is added");
            addCandidate = repoSubItem.addSubItem(subItem.getIdentity(), candidate);
        }
        log.trace("updatePopularSubItems, removeSubItems:{}", toBeDeleted);

        Flux<IdScore> removeAll = Flux.fromIterable(toBeDeleted)
                .flatMap(idScore -> repoSubItem.removeSubItem(subItem.getIdentity(), idScore));

        return addCandidate.thenMany(removeAll).then(Mono.just(subItem));
    }

    public static <T extends ReputationCounter> Mono<T> updatePopularSubItems(T reputationCounter) {

        if (reputationCounter.isOverThreshold() == false) {
            return Mono.just(reputationCounter);
        }

        return repoSubItem.findById(reputationCounter.getContainerId())
                .defaultIfEmpty(SubItem.of(reputationCounter.getContainerId()))
                .flatMap(subItem -> updatePopularSubItems(getMaxSubItems(reputationCounter.getClass()),
                                                          subItem,
                                                          reputationCounter.toIdScore()))
                .thenReturn(reputationCounter);
    }

}
