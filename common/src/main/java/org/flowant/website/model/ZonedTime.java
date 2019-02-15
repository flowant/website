package org.flowant.website.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.flowant.website.util.TimeUtil;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class ZonedTime {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDateTime time;

    public static ZonedTime now() {
        return ZonedTime.of(ZoneId.systemDefault(), TimeUtil.nowMillisecond());
    }

}
