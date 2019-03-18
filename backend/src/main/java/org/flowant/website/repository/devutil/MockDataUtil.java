package org.flowant.website.repository.devutil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PreDestroy;

import org.flowant.website.WebSiteConfig;
import org.flowant.website.event.MockDataGenerateEvent;
import org.flowant.website.model.Category;
import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.FileRef;
import org.flowant.website.model.Message;
import org.flowant.website.model.Notification;
import org.flowant.website.model.Reply;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.User;
import org.flowant.website.model.WebSite;
import org.flowant.website.repository.ContentRepository;
import org.flowant.website.repository.ContentReputationRepository;
import org.flowant.website.repository.MessageRepository;
import org.flowant.website.repository.NotificationRepository;
import org.flowant.website.repository.RelationRepository;
import org.flowant.website.repository.RelationshipService;
import org.flowant.website.repository.ReplyRepository;
import org.flowant.website.repository.ReplyReputationRepository;
import org.flowant.website.repository.ReviewRepository;
import org.flowant.website.repository.ReviewReputationRepository;
import org.flowant.website.repository.SubItemRepository;
import org.flowant.website.repository.UserRepository;
import org.flowant.website.repository.WebSiteRepository;
import org.flowant.website.rest.FileRest;
import org.flowant.website.storage.FileStorage;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.ReplyMaker;
import org.flowant.website.util.test.ReputationMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class MockDataUtil {

    @Value("${server.port}")
    int port;

    @Value("${server.address}")
    String address;

    @Autowired
    WebSiteConfig config;

    @Autowired
    WebSiteRepository repoWebSite;

    @Autowired
    ContentRepository repoContent;

    @Autowired
    ReviewRepository repoReview;

    @Autowired
    ReplyRepository repoReply;

    @Autowired
    ContentReputationRepository repoContentRpt;

    @Autowired
    ReviewReputationRepository repoReviewRpt;

    @Autowired
    ReplyReputationRepository repoReplyRpt;

    @Autowired
    SubItemRepository repoSubItem;

    @Autowired
    NotificationRepository repoNotification;

    @Autowired
    MessageRepository repoMessage;

    @Autowired
    RelationRepository repoRelation;

    @Autowired
    UserRepository repoUser;

    WebSite webSite;

    Flux<Content> contents = Flux.empty();

    Flux<User> users = Flux.empty();

    Set<UUID> userSet = Set.of();

    int cntUsersContents = 20;

    int cntRepliesPerReview = 3;

    public FileRef postRandomFile(Optional<String> pathSegId) {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        Random random = new Random();
        int pictureIndex = random.nextInt(3) + 1;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ClassPathResource> entity =
                new HttpEntity<>(new ClassPathResource("sea" + pictureIndex + ".jpg"), headers);
        parts.add(FileRest.ATTACHMENT, entity);

        return WebClient
                .create("http://" + address + ":" + port + FileRest.PATH_FILES + pathSegId.orElse(""))
                .post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange()
                .block()
                .bodyToFlux(FileRef.class)
                .blockLast();
    }

    public Content saveContent(Content content) {

        content.setFileRefs(List.of(postRandomFile(Optional.empty())));

        Mono<Content> contentM = Mono.just(content).cache();
        contentM.flatMap(repoContent::save).block();
        contentM.map(c -> Notification.fromAuthor(c, Category.NC, userSet))
                .flatMap(repoNotification::save)
                .block();

        Mono<ContentReputation> contentReputation =
                contentM.map(c -> ReputationMaker.randomContentReputation(c.getIdCid()))
                        .cache();
        contentReputation.flatMap(repoContentRpt::save).block();

        // make one review per user at a content
        Flux<Review> reviews = users
                .map(user -> ReviewMaker.largeRandom(contentM.block().getIdentity()).setAuthor(user))
                .cache();
        reviews.flatMap(repoReview::save).blockLast();
        reviews.map(r -> Notification.fromAuthor(r, Category.NRV, Set.of(content.getAuthorId())))
                .flatMap(repoNotification::save)
                .blockLast();

        Flux<ReviewReputation> reviewReputations = reviews
                .map(r -> ReputationMaker.randomReviewReputation(r.getIdCid()))
                .cache();
        reviewReputations.flatMap(repoReviewRpt::save).blockLast();

        // make cntRepliesPerReview replies per review.
        Flux<Reply> replies = reviews
                .flatMap(review -> Flux.range(1, cntRepliesPerReview)
                        .map(i -> ReplyMaker.largeRandom(review.getIdentity()).setAuthor(review))
                        .cast(Reply.class))
                .cache();
        replies.flatMap(repoReply::save).blockLast();
        replies.map(r -> Notification.fromAuthor(r, Category.NRV, Set.of(r.getAuthorId())))
                .flatMap(repoNotification::save)
                .blockLast();

        Flux<ReplyReputation> replyReputations = replies
                .map(r -> ReputationMaker.randomReplyReputation(r.getIdCid())).cache();
        replyReputations.flatMap(repoReplyRpt::save).blockLast();

        return contentM.block();
    }

    public void sendMessage(User sender) {
        users.map(user -> Message.fromUser(user.getIdentity(), sender, "message contents" + sender.getIdentity()))
                .flatMap(repoMessage::save)
                .blockLast();
    }

    public void follow(User follower) {
        users.flatMap(followee -> repoRelation.follow(follower.getIdentity(), followee.getIdentity())).blockLast();
    }

    public void saveMockData() {

        UUID recipeCid = UUID.fromString(config.getContentContainerIds().get(WebSiteConfig.RECIPE));
        UUID userId = UUID.fromString("b901f010-4546-11e9-97e9-594de5a6cf90");

        webSite = WebSite.builder()
                .identity(UUID.fromString(config.getIdentity()))
                .contentContainerIds(Map.of(WebSiteConfig.RECIPE, recipeCid))
                .build();
        repoWebSite.save(webSite).block();

        users = Flux.range(1, cntUsersContents - 1)
                .map(i -> UserMaker.largeRandom())
                .concatWith(Flux.just(UserMaker.large(userId)))
                .cache();

        users.doOnNext(user -> user.setFileRefs(List.of(postRandomFile(Optional.of("/" + user.getIdentity()))))).blockLast();

        userSet = Set.copyOf(users.map(User::getIdentity).collectList().block());
        contents = users.map(user -> ContentMaker.largeRandom(recipeCid).setAuthor(user)).cache();

        repoUser.saveAll(users).blockLast();
        contents.map(c -> saveContent(c)).blockLast();

        users.subscribe(this::sendMessage);

        users.subscribe(this::follow);
    }

    @PreDestroy
    public void onPreDestroy() throws Exception {

        contents.flatMap(c -> FileStorage.deleteAll(c.getFileRefs())
                .then(repoContent.deleteByIdWithRelationship(c.getIdCid()))
                .then(RelationshipService.deleteSubItemById(c.getContainerId())))
                .blockLast();

        repoWebSite.delete(webSite).block();

        users.flatMap(u -> FileStorage.deleteAll(u.getFileRefs())
                .then(repoUser.delete(u))).blockLast();

        users.flatMap(user -> repoNotification.deleteAllByIdCidContainerId(user.getIdentity())).blockLast();

        users.flatMap(user -> repoMessage.deleteAllByIdCidContainerId(user.getIdentity())).blockLast();

        users.flatMap(user -> repoRelation.deleteById(user.getIdentity())).blockLast();

        log.debug("Mock data are deleted before shutting down.");
    }

    @EventListener
    public void onApplicationEvent(MockDataGenerateEvent event) {
        log.debug(event::toString);
        saveMockData();
    }

}
