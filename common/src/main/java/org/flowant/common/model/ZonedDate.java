package org.flowant.common.model;

import java.time.LocalDate;
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
public class ZonedDate {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDate localDate;

    public static ZonedDate now() {
        return ZonedDate.of(ZoneId.systemDefault(), LocalDate.now());
    }
}
