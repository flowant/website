package org.flowant.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@Table
public class Content {
    @Id @NonNull
    UUID id;
    @NonNull
    String title; // to be tags allways
    Recipe extend; // TODO extend
    List<Paragraph> paragraphs = new ArrayList<>();
    Review review = new Review();
    List<Tag> tags = new ArrayList<>();
    Authority accessLevel = Authority.ANONYMOUS;
    CRUDZonedTime crudTime = CRUDZonedTime.now();
}
