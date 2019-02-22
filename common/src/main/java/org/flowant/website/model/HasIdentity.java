package org.flowant.website.model;

import java.util.UUID;

public interface HasIdentity {

    UUID getIdentity();

    HasIdentity setIdentity(UUID identity);

}
