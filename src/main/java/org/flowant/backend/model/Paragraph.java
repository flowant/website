package org.flowant.backend.model;

import java.nio.ByteBuffer;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType
public class Paragraph {
    ByteBuffer multimedia;
    String sentences;
}
