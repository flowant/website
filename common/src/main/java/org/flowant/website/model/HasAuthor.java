package org.flowant.website.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasAuthor<T> {

    UUID getAuthorId();

    String getAuthorName();

    T setAuthorId(UUID authorId);

    T setAuthorName(String authorName);

    @JsonIgnore
    default T setAuthor(User user) {
        setAuthorId(user.getIdentity());
        return setAuthorName(user.getDisplayName());
    }

    @JsonIgnore
    default T setAuthor(HasAuthor<?> hasAuthor) {
        setAuthorId(hasAuthor.getAuthorId());
        return setAuthorName(hasAuthor.getAuthorName());
    }

}
