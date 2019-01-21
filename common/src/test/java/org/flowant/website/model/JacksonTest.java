package org.flowant.website.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.flowant.website.model.Content;
import org.flowant.website.model.Tag;
import org.flowant.website.model.User;
import org.flowant.website.util.test.ContentMaker;
import org.flowant.website.util.test.UserMaker;
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
    public <T> void testWithJson(T t) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(t);
        log.debug("Json:{}", json);
        Object obj = objectMapper.readValue(json, t.getClass());
        log.debug("ToString:{}", obj);
    }

    @Test
    public void testTagWithJson() throws JsonGenerationException, JsonMappingException, IOException {
        testWithJson(Tag.of("name"));
    }

    @Test
    @Parameters
    public void testUser(User user) throws JsonGenerationException, JsonMappingException, IOException {
        testWithJson(user);
    }
    public static List<User> parametersForTestUser() {
      return Arrays.asList(UserMaker.small(), UserMaker.large());
    }

    @Test
    @Parameters
    public void testContent(Content content) throws JsonGenerationException, JsonMappingException, IOException {
        testWithJson(content);
    }
    public static List<Content> parametersForTestContent() {
      return Arrays.asList(ContentMaker.small(), ContentMaker.large());
    }

}
