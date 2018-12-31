package org.flowant.users.data;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@UserDefinedType
public class ZonedTime {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDateTime time;

    public static ZonedTime now() {
        return ZonedTime.builder().zoneId(ZoneId.systemDefault()).time(LocalDateTime.now()).build();
    }

}
