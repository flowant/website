package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.Content;

public interface ContentRepository extends PageableRepository<Content, UUID> {
}
