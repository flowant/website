package org.flowant.backend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data
@UserDefinedType
public class Review {
    Reputation reputation = new Reputation();
    List<Reply> replies = new ArrayList<>();
}
