package org.flowant.website.util.test;

import java.util.UUID;

import org.flowant.website.model.IdCid;
import org.flowant.website.model.Report;
import org.flowant.website.model.ZonedTime;
import org.flowant.website.util.IdMaker;

public class ReportMaker {

    static UUID reporterId = IdMaker.randomUUID();
    static String reporterName = "reporterName";
    static String sentences = "sentences";

    public static Report random(IdCid idCid) {
        String suffix = idCid.getIdentity().toString().substring(0, 8);
        return Report.of(idCid, reporterId, reporterName + suffix, sentences + suffix, ZonedTime.now());
    }

    public static Report random(UUID containerId) {
        return random(IdCid.random(containerId));
    }

    public static Report random() {
        return random(IdCid.random());
    }

}
