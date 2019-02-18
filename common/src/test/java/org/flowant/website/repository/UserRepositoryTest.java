package org.flowant.website.repository;

import java.util.UUID;

import org.flowant.website.model.User;
import org.flowant.website.util.test.UserMaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import com.datastax.driver.core.PagingState;

import junitparams.JUnitParamsRunner;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
@Log4j2
public class UserRepositoryTest extends RepositoryTest<User, UUID, UserRepository> {

    static final String lastname = "Jee";

    @Test
    public void crud() {
        testCrud(User::getIdentity, User::getIdentity, UserMaker::smallRandom, UserMaker::largeRandom);
    }

    @Test
    public void findByUsername() {
        User user = UserMaker.largeRandom();
        registerToBeDeleted(user);

        Flux<User> saveThenFind = repo.save(user).thenMany(repo.findByUsername(user.getUsername()));
        StepVerifier.create(saveThenFind).expectNextMatches(u -> user.getUsername().equals(u.getUsername()))
                .verifyComplete();
    }

    @Test
    public void queryOptions() {
        User user = UserMaker.largeRandom();
        registerToBeDeleted(user);

        repo.save(user).block();

        //TODO check
        QueryOptions options = QueryOptions.builder().readTimeout(1).build();
        log.trace(repo.findByUsername(user.getUsername(), options).blockLast());
    }

    public Mono<Slice<User>> getAllPaging(int page, int size, @Nullable String pagingState) {
        log.trace("getAllPaging, page:{}, size:{}, pagingState:{}", page, size, pagingState);
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pagingState != null) {
            pageRequest = CassandraPageRequest.of(pageRequest, PagingState.fromString(pagingState));
        }
        log.trace("getAllPaging, pageRequest:{}", pageRequest);
        return repo.findByLastname(lastname, pageRequest);
    }

    public Mono<Slice<User>> getAllPaging(Pageable pageable) {
        log.trace("getAllPaging, pageable:{}", pageable);
        CassandraPageRequest cpr = CassandraPageRequest.class.cast(pageable);
        return getAllPaging(cpr.getPageNumber(), cpr.getPageSize(),
                cpr.getPagingState().toString());
    }

    @Test
    public void sliceAndPageable() {
        Flux<User> users = Flux.range(1, 10).map(i -> UserMaker.smallRandom().setLastname(lastname)).cache();
        registerToBeDeleted(users);
        repo.saveAll(users).blockLast();

        Slice<User> slice = getAllPaging(0, 3, null).block();
        while(true) {
            if (slice.hasContent()) {
                slice.forEach(log::trace);
            }
            if (!slice.hasNext()) {
                break;
            }
            slice = getAllPaging(slice.nextPageable()).block();
        }
    }

    @Test
    public void sliceAndPageablePrev() {
        Flux<User> users = Flux.range(1, 10).map(i -> UserMaker.smallRandom().setLastname(lastname)).cache();
        registerToBeDeleted(users);
        repo.saveAll(users).blockLast();

        Slice<User> slice = getAllPaging(0, 3, null).block();
        slice.forEach(log::trace);

        slice = getAllPaging(slice.nextPageable()).block();
        slice.forEach(log::trace);

        slice = getAllPaging(slice.nextPageable()).block();
        slice.forEach(log::trace);

        // below code throws cast Exception, because previousPageable() method
        // returns very first pageable object only, not updated.
        // Also, pageNumber is not updated and is ignored.
        // TODO check again after version is upgraded.
        // slice = getAllPaging(slice.previousPageable()).block();
        // slice.forEach(log::trace);
    }

}
