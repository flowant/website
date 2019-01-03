package org.flowant.backend.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class CRUDZonedTime {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDateTime created; // TODO auto fill
    @NonNull
    LocalDateTime read; // TODO auto fill
    @NonNull
    LocalDateTime updated; // TODO auto fill
    LocalDateTime deleted;// TODO trash can function

    public static CRUDZonedTime now() {
        LocalDateTime t = LocalDateTime.now();
        return new CRUDZonedTime(ZoneId.systemDefault(), t, t, t);
    }
}
