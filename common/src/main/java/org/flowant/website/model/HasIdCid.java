package org.flowant.website.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasIdCid extends HasIdentity, HasContainerId {

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

}
