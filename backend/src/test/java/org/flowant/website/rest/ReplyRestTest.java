package org.flowant.website.rest;

import java.util.UUID;

import org.flowant.website.BackendApplication;
import org.flowant.website.model.Reply;
import org.flowant.website.repository.BackendReplyRepository;
import org.flowant.website.util.test.ReplyMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes=BackendApplication.class)
public class ReplyRestTest extends BaseRestWithRepositoryTest<Reply, UUID, BackendReplyRepository> {

    @Test
    public void testCrud() {
        super.testCrud(ReplyRest.REPLY, Reply.class, Reply::getId,
                ReplyMaker::smallRandom, ReplyMaker::largeRandom,
                (Reply r) -> {
                    r.setComment("newComment");
                    return r;}
                );
    }

}
