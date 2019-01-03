package org.flowant.backend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileUtilTest {
    String path = null;

    @Before
    public void setUp() {
        path = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() throws IOException {
    }

    @Test
    public void writeReadStringToFile() throws IOException {
        FileUtil.rmdirs(path);
        String contents = this.getClass().getCanonicalName();
        FileUtil.writeStringToFile(path, contents);
        String readContents = FileUtil.readStringFromLittleFile(path);
        Assert.assertEquals(readContents, contents);
        FileUtil.rmdirs(path);
    }

    @Test
    public void toSafePath() throws IOException {
        String orgPath = "\"/?\\*<>|EARTH, expDlr, skip:0, limit:10, 201507 ~ 201704.jpg";
        String encodedPath = FileUtil.toSafePath(orgPath);
        log.debug(() -> "orgPath:" + orgPath);
        log.debug(() -> "encodedPath:" + encodedPath);
        Path path = Paths.get(encodedPath);
        Files.createFile(path);
        Assert.assertTrue(Files.exists(path));
        FileUtil.rmdirs(path);
    }
}
