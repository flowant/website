package org.flowant.backend.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RunWith(JUnitParamsRunner.class)
public class JacksonTest {

    @Test
    @Parameters
    public void testUser(User user) throws JsonGenerationException, JsonMappingException, IOException {
        testWithJson(user);
    }
    public static List<User> parametersForTestUser() {
      return Arrays.asList(UserMaker.small(), UserMaker.large());
    }

    @Test
    public void testTagWithJson() throws JsonGenerationException, JsonMappingException, IOException {
        testWithJson(Tag.of("name"));
    }

    public <T> void testWithJson(T t) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(t);
        log.debug("Json:{}", json);
        Object obj = objectMapper.readValue(json, t.getClass());
        log.debug("ToString:{}", obj);
    }
}
