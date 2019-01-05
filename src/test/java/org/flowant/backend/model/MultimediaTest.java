package org.flowant.backend.model;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MultimediaTest {

    static UUID id;
    static UUID contentId;
    static ByteBuffer media = ByteBuffer.wrap("MultimediaTest".getBytes());
    static String contentType = "contentType";
    static String originalFilename = "originalFilename";

    public static Multimedia large(int s) {
        return Multimedia.of(UUID.randomUUID(), UUID.randomUUID(), media.putInt(0, s), contentType + s,
                originalFilename + s, CRUDZonedTime.now());
    }

    public static Multimedia large() {
        return large(0);
    }

    @Test
    public void testMaker() {
        log.debug("Multimedia:{}", large()::toString);
    }

}
