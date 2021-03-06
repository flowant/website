package org.flowant.website.repository;

import java.util.Comparator;
import java.util.UUID;

import org.flowant.website.model.Report;
import org.flowant.website.util.IdMaker;
import org.flowant.website.util.test.ReportMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import com.datastax.driver.core.utils.UUIDs;

import junitparams.JUnitParamsRunner;
import reactor.core.publisher.Flux;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class ReportRepositoryTest extends IdCidRepositoryTest<Report, ReportRepository> {

    @Test
    public void crud() {
        testCrud(Report::getIdCid, ReportMaker::random, ReportMaker::random);
    }

    @Test
    public void pageable() {
        UUID containerId = UUIDs.timeBased();
        Flux<Report> entities = Flux.range(1, 10).map(i -> ReportMaker.random(containerId));
        findAllByContainerIdPageable(containerId, entities);
    }

    @Test
    public void findAllByAuthorIdPageable() {
        UUID authorId = IdMaker.randomUUID();
        Flux<Report> entities = Flux.range(1, 10)
                .map(i -> ReportMaker.random().setAuthorId(authorId)).cache();
        cleaner.registerToBeDeleted(entities);
        saveAndGetPaging(entities, pageable -> repo.findAllByAuthorId(authorId, pageable));
    }

    @Test
    public void ordered() {
        testOrdered(Report::getIdCid, Comparator.comparing(Report::getIdentity).reversed(),
                ReportMaker::random);
    }

    @Test
    public void testDeleteAllByContainerId() {
        super.testDeleteAllByContainerId(ReportMaker.random(), Report::getContainerId);
    }

}
