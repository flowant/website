package org.flowant.backend.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.flowant.backend.model.Multimedia;
import org.flowant.backend.model.MultimediaInfo;
import org.flowant.backend.model.MultimediaTest;
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
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
@Log4j2
public class MultimediaRepositoryTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	MultimediaRepository multimediaRepository;

	Consumer<? super Multimedia> deleteMultimedia = c -> multimediaRepository.delete(c).subscribe();
	Consumer<? super Collection<Multimedia>> deleteMultimedias = l -> l.forEach(deleteMultimedia);

	@Test
    @Parameters
    public void testSave(Multimedia multimedia) {
        Flux<Multimedia> saveThenFind = multimediaRepository.save(multimedia).thenMany(multimediaRepository.findById(multimedia.getId()));
        StepVerifier.create(saveThenFind).consumeNextWith(deleteMultimedia).verifyComplete();
    }
    public static List<Multimedia> parametersForTestSave() {
        return Arrays.asList(MultimediaTest.large());
    }

    @Test
    public void testSaveAllFindAllById() {
        Flux<Multimedia> multimedia = Flux.range(1, 5).map(MultimediaTest::large).cache();
        Flux<Multimedia> saveAllThenFind = multimediaRepository.saveAll(multimedia)
                .thenMany(multimediaRepository.findAllById(multimedia.map(Multimedia::getId)));
        StepVerifier.create(saveAllThenFind).recordWith(ArrayList::new).expectNextCount(5)
        .consumeRecordedWith(deleteMultimedias).verifyComplete();
    }

    public void testSaveDebug() {
        Multimedia multimedia = MultimediaTest.large();
        MultimediaInfo mInfo = multimediaRepository.save(multimedia).map(MultimediaInfo.class::cast).block();
        log.trace("MultimediaInfo: {}", mInfo::toString);
    }
}
