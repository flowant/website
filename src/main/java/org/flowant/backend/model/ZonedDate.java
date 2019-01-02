package org.flowant.backend.model;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class ZonedDate {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDate localDate;

    public static ZonedDate now() {
        return ZonedDate.of(ZoneId.systemDefault(), LocalDate.now());
    }
}
