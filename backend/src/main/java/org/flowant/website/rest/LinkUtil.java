package org.flowant.website.rest;

import static org.flowant.website.rest.PageableRepositoryRest.CID;
import static org.flowant.website.rest.PageableRepositoryRest.PAGE;
import static org.flowant.website.rest.PageableRepositoryRest.PS;
import static org.flowant.website.rest.PageableRepositoryRest.SIZE;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import com.datastax.driver.core.PagingState;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LinkUtil {

    public static Pageable pageable(int page, int size, @Nullable String pagingState) {
        log.trace("getAllPaging, page:{}, size:{}, pagingState:{}", page, size, pagingState);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pagingState != null) {
            pageRequest = CassandraPageRequest.of(pageRequest, PagingState.fromString(pagingState));
        }
        return pageRequest;
    }

    public static HttpHeaders nextLinkHeader(String containerId, UriComponentsBuilder uriBuilder, Slice<?> slice) {
        if (slice.hasNext() == false) {
            return null;
        }
        CassandraPageRequest pageable = CassandraPageRequest.class.cast(slice.nextPageable());
        String url = uriBuilder
                .queryParam(CID, containerId)
                .queryParam(PAGE, pageable.getPageNumber())
                .queryParam(SIZE, pageable.getPageSize())
                .queryParam(PS, pageable.getPagingState())
                .build().encode().toUriString();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.LINK, "<" + url + ">; rel=\"next\"");
        return header;
    }

    public static Optional<URI> getNextUrl(HttpHeaders headers) {
        List<String> links = headers.get(HttpHeaders.LINK);
        if (links == null) {
            return Optional.empty();
        }
        Optional<String> optValue = links.stream().filter(s -> s.contains("rel=\"next\"")).findAny();
        if (optValue.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(URI.create(parseNextUrl(optValue.get())));
    }

    protected static String parseNextUrl(String value) {
        int s = value.indexOf('<');
        int e = value.indexOf('>');
        Assert.isTrue(s != -1 && e != -1 && s < e, "must contain '<' and '>' characters");
        return value.substring(s + 1, e);
    }
}
