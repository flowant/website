package org.flowant.website.repository;

import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import com.datastax.driver.core.PagingState;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PageableUtil {
    public static Pageable pageable(int page, int size, @Nullable String pagingState) {
        log.trace("getAllPaging, page:{}, size:{}, pagingState:{}", page, size, pagingState);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pagingState != null) {
            pageRequest = CassandraPageRequest.of(pageRequest, PagingState.fromString(pagingState));
        }
        return pageRequest;
    }
}
