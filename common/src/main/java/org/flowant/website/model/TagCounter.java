package org.flowant.website.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType.Name;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class TagCounter {

    @Id
    @Column("t")
    String tag;

    @CassandraType(type=Name.COUNTER)
    @Column("s")
    long searched;

    @CassandraType(type=Name.COUNTER)
    @Column("r")
    long referred;

}
