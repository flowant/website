package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.FileRef;

public class FileMaker {

    static UUID id;
    static String url = "url";
    static String contentType = "contentType";
    static String filename = "originalFilename";

    public static FileRef large(UUID id) {
        return FileRef.of(id, url + id,
                contentType + id, filename + id, CRUZonedTime.now());
    }

    public static FileRef largeRandom() {
        return large(UUID.randomUUID());
    }

}
