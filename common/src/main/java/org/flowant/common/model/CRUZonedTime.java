package org.flowant.common.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.flowant.common.util.TimeUtil;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class CRUZonedTime {
    @NonNull
    ZoneId zoneId;
    @NonNull
    LocalDateTime created; // TODO auto fill
    @NonNull
    LocalDateTime read; // TODO auto fill
    @NonNull
    LocalDateTime updated; // TODO auto fill

    public static CRUZonedTime now() {
        LocalDateTime t = TimeUtil.nowMillisecond();
        return new CRUZonedTime(ZoneId.systemDefault(), t, t, t);
    }

    public CRUZonedTime createdNow() {
        created = read = updated = TimeUtil.nowMillisecond();
        return this;
    }

    public CRUZonedTime readNow() {
        read = TimeUtil.nowMillisecond();
        return this;
    }

    public CRUZonedTime updatedNow() {
        updated = TimeUtil.nowMillisecond();
        return this;
    }
}
