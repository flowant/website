package org.flowant.website;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.flowant.website.model.Content;
import org.flowant.website.model.ContentReputation;
import org.flowant.website.model.ReplyReputation;
import org.flowant.website.model.Review;
import org.flowant.website.model.ReviewReputation;
import org.flowant.website.model.WebSite;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Component
@Getter @Setter @ToString
@ConfigurationProperties(prefix = "website")
@Log4j2
public class WebSiteConfig {

    int maxSubContentItems;
    int maxSubReviewItems;
    int maxSubReplyItems;
    long ttlNotifications;

    static HashMap<Class<?>, Integer> mapMaxSubItems = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        log.debug(this::toString);

        mapMaxSubItems.put(WebSite.class, maxSubContentItems);
        mapMaxSubItems.put(ContentReputation.class, maxSubContentItems);
        mapMaxSubItems.put(Content.class, maxSubReviewItems);
        mapMaxSubItems.put(ReviewReputation.class, maxSubReviewItems);
        mapMaxSubItems.put(Review.class, maxSubReplyItems);
        mapMaxSubItems.put(ReplyReputation.class, maxSubReplyItems);
    }

    public static int getMaxSubItems(Class<?> cls) {
        return mapMaxSubItems.get(cls);
    }

}
