package org.flowant.backend.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class ZonedTime {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDateTime time;

    public static ZonedTime now() {
        return ZonedTime.of(ZoneId.systemDefault(), LocalDateTime.now());
    }

}
