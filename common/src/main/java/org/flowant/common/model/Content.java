package org.flowant.common.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class Content {
    @Id @NonNull
    UUID id;
    @NonNull
    String title; // to be tags always
    Recipe extend; // TODO how to extend gracefully?
    List<FileRef> fileRefs;
    String sentences;
    List<Tag> tags;
    @NonNull
    Review review;
    @Builder.Default
    Authority accessLevel = Authority.ANONYMOUS;
    @NonNull
    CRUZonedTime cruTime;
}
