package org.flowant.website.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class TagCount {

    @Id
    String tag;

    long searched;

    long referred;

}
