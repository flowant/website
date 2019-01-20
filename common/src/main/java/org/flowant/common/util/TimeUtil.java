package org.flowant.common.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static LocalDateTime nowMillisecond() {
        LocalDateTime t = LocalDateTime.now();
        return t.withNano((t.getNano() / 1000000) * 1000000);
    }
}
