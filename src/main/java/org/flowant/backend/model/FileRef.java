package org.flowant.backend.model;

import java.util.UUID;

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
public class FileRef {
    @NonNull
    String url;
    UUID fileId; // File's ID can be null when no file is uploaded
}
