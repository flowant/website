package org.flowant.website.rest;

import static org.flowant.website.rest.IdCidRepositoryRest.PAGE;
import static org.flowant.website.rest.IdCidRepositoryRest.PS;
import static org.flowant.website.rest.IdCidRepositoryRest.SIZE;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

public class LinkUtil {

    public static HttpHeaders nextLinkHeader(String paramName, String paramValue, UriComponentsBuilder uriBuilder, Slice<?> slice) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(paramName, paramValue);
        return nextLinkHeader(params, uriBuilder, slice);
    }

    public static HttpHeaders nextLinkHeader(MultiValueMap<String, String> params, UriComponentsBuilder uriBuilder, Slice<?> slice) {
        if (slice.hasNext() == false) {
            return null;
        }
        CassandraPageRequest pageable = CassandraPageRequest.class.cast(slice.nextPageable());
        String url = uriBuilder
                .queryParams(params)
                .queryParam(PAGE, pageable.getPageNumber())
                .queryParam(SIZE, pageable.getPageSize())
                .queryParam(PS, pageable.getPagingState())
                .build().encode().toUriString();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.LINK, "<" + url + ">; rel=\"next\"");
        header.add("Access-Control-Expose-Headers", "Link");
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
