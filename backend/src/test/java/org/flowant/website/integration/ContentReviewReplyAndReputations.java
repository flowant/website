package org.flowant.website.integration;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Content;
import org.flowant.website.model.Review;
import org.flowant.website.model.User;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.ReviewMaker;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes=BackendApplication.class)
@Log4j2
public class ContentReviewReplyAndReputations extends BaseIntegrationTest {

    int cntUsers = 100;

    @Test
    public void dummy() {
        Content content = ContentMaker.largeRandom();
        post(content, Content.class);
        getById(content.getId(), Content.class);

        User user = UserMaker.largeRandom();
        post(user, User.class);
        getById(user.getId(), User.class);
    }

//    @Test
    public void ContentReviewAndReputations() {

        Flux<User> users = Flux.range(1, cntUsers).map(i -> UserMaker.largeRandom()).cache();

        Content content = ContentMaker.largeRandom();

        Flux<Review> reviews = Flux.range(1, cntUsers).map(i -> ReviewMaker.largeRandom()).cache();

        // 100 users
        

        
        // 100 review
        
        // 100 * 100 like
        
        // getcontent with review
        
        // review with counter
        
        // content reputation
        
        
        
    }
}
