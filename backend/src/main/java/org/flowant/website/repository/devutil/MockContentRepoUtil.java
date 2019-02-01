package org.flowant.website.repository.devutil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.flowant.website.event.MockDataGenerateEvent;
import org.flowant.website.model.Content;
import org.flowant.website.repository.BackendContentRepository;
import org.flowant.website.util.test.ContentMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class MockContentRepoUtil {

    @Autowired
    BackendContentRepository contentRepository;
    List<Content> mocks = new ArrayList<>();

    public Content saveContent(Content content) {
        contentRepository.save(content).block();
        return content;
    }

    public void saveMockContents(int cntContent) {
        int count = contentRepository.count().block().intValue();
        for (int i = count; i < cntContent; i++) {
            Content content = ContentMaker.largeRandom();
            mocks.add(saveContent(content));
            log.debug("saved mock content:{}", content);
        }
    }

    @EventListener
    public void onApplicationEvent(MockDataGenerateEvent event) {
        log.debug(event::toString);
        saveMockContents(20);
    }

    @PreDestroy
    public void onPreDestroy() throws Exception {
        if (mocks.size() > 0) {
            contentRepository.deleteAll(mocks).block();
            log.debug("Mock data are deleted before shutting down.");
        }
    }

}
