package org.flowant.website.model;

import java.util.UUID;

public interface HasIdentity extends Comparable<HasIdentity> {
    UUID getIdentity();
    HasIdentity setIdentity(UUID identity);

    default public int compareTo(HasIdentity hasIdentity) {
        return getIdentity().compareTo(hasIdentity.getIdentity());
    }
}
