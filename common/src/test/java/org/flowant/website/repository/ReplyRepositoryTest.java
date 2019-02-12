package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Reply;
import org.flowant.website.util.test.ReplyMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReplyRepositoryTest extends BaseRepositoryTest<Reply, UUID, ReplyRepository> {

    @Test
    public void crud() {
        testCrud(Reply::getId, ReplyMaker::smallRandom, ReplyMaker::largeRandom);
    }
}
