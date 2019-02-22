package org.flowant.website.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasIdCid extends Comparable<HasIdCid> {

    IdCid getIdCid();

    HasIdCid setIdCid(IdCid idCid);

    @JsonIgnore
    default UUID getContainerId() {
        return getIdCid().getContainerId();
    }

    @JsonIgnore
    default UUID getIdentity() {
        return getIdCid().getIdentity();
    }

    default public int compareTo(HasIdCid hasIdCid) {
        return getIdentity().compareTo(hasIdCid.getIdentity());
    }

}
