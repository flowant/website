package org.flowant.backend.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.flowant.backend.model.Content;
import org.flowant.backend.model.ContentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ContentRepositoryTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	ContentRepository contentRepository;

	Consumer<? super Content> deleteContent = c -> contentRepository.delete(c).subscribe();
	Consumer<? super Collection<Content>> deleteContents = l -> l.forEach(deleteContent);

	@Test
    @Parameters
    public void testSave(Content content) {
        Flux<Content> saveThenFind = contentRepository.save(content).thenMany(contentRepository.findById(content.getId()));
        StepVerifier.create(saveThenFind).consumeNextWith(deleteContent).verifyComplete();
    }
    public static List<Content> parametersForTestSave() {
        return Arrays.asList(ContentTest.small(), ContentTest.large());
    }

    @Test
    public void testSaveAllFindAllById() {
        Flux<Content> contents = Flux.range(1, 5).map(ContentTest::large).cache();
        Flux<Content> saveAllThenFind = contentRepository.saveAll(contents)
                .thenMany(contentRepository.findAllById(contents.map(Content::getId)));
        StepVerifier.create(saveAllThenFind).recordWith(ArrayList::new).expectNextCount(5)
        .consumeRecordedWith(deleteContents).verifyComplete();
    }
}
