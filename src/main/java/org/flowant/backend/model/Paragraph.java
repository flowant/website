package org.flowant.backend.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data(staticConstructor = "of")
@UserDefinedType
public class Paragraph {
    ByteBuffer multimedia;
    String sentences;
}
