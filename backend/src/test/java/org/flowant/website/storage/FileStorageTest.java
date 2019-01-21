package org.flowant.website.storage;

import java.io.IOException;

import org.flowant.website.BackendApplication;
import org.flowant.website.storage.FileStorage;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RunWith(JUnitParamsRunner.class)
@SpringBootTest(classes=BackendApplication.class)
public class FileStorageTest {
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Test
    public void testFindAll () throws IOException {
        FileStorage.findAll().map(Resource::getFilename).collectList().subscribe(log::trace);
    }
}
