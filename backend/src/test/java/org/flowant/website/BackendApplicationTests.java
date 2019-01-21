package org.flowant.website;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=BackendApplication.class)
@Log4j2
public class BackendApplicationTests {
    @Test
    public void contextLoads() {
        log.debug("from contextLoads");
    }
}
