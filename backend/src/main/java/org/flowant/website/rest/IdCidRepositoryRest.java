package org.flowant.website.rest;

import org.flowant.website.model.HasIdCid;
import org.flowant.website.model.IdCid;
import org.flowant.website.repository.IdCidRepository;

public abstract class IdCidRepositoryRest <T extends HasIdCid, R extends IdCidRepository<T>>
        extends RepositoryRest<T, IdCid, R> {

    public final static String CID = "cid";
    public final static String PATH_SEG_ID_CID = "/{id}/{cid}";

}
