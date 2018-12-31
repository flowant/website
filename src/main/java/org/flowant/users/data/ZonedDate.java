package org.flowant.users.data;

import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
@UserDefinedType
public class ZonedDate {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDate localDate;

    public static ZonedDate now() {
        return ZonedDate.builder().zoneId(ZoneId.systemDefault()).localDate(LocalDate.now()).build();
    }
}
