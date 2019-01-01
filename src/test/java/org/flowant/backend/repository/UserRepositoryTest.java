package org.flowant.backend.repository;

import java.util.UUID;

import org.flowant.backend.model.CRUDZonedTime;
import org.flowant.backend.model.User;
import org.flowant.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import example.springdata.cassandra.util.CassandraKeyspace;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@ClassRule public final static CassandraKeyspace CASSANDRA_KEYSPACE = CassandraKeyspace.onLocalhost();

	@Autowired UserRepository repository;

	/**
	 * Clear table and insert some rows.
	 */
	@Before
	public void setUp() {
	    
	    Flux<User> users = Flux.range(1, 5).map(i -> User.of(UUID.randomUUID(),
                "username" + i, "password" + i, "email" + i, CRUDZonedTime.now()));
	    
		Flux<User> deleteAndInsert = repository.deleteAll()
				.thenMany(repository.saveAll(users));
		
		StepVerifier.create(deleteAndInsert).expectNextCount(5).verifyComplete();
	}

    @Test
    public void testFindAll() {
        StepVerifier.create(repository.findAll().doOnNext(System.out::println))
                .expectNextCount(5)
                .verifyComplete();
    }
	
//	/**
//	 * This sample performs a count, inserts data and performs a count again using reactive operator chaining.
//	 */
//	@Test
//	public void shouldInsertAndCountData() {
//
//		Mono<Long> saveAndCount = repository.count() //
//				.doOnNext(System.out::println) //
//				.thenMany(repository.saveAll(Flux.just(new Person("Hank", "Schrader", 43), //
//						new Person("Mike", "Ehrmantraut", 62)))) //
//				.last() //
//				.flatMap(v -> repository.count()) //
//				.doOnNext(System.out::println);
//
//		StepVerifier.create(saveAndCount).expectNext(6L).verifyComplete();
//	}
//
//	/**
//	 * Result set {@link com.datastax.driver.core.Row}s are converted to entities as they are emitted. Reactive pull and
//	 * prefetch define the amount of fetched records.
//	 */

//
//	/**
//	 * Fetch data using query derivation.
//	 */
//	@Test
//	public void shouldQueryDataWithQueryDerivation() {
//		StepVerifier.create(repository.findByLastname("White")).expectNextCount(2).verifyComplete();
//	}
//
//	/**
//	 * Fetch data using a string query.
//	 */
//	@Test
//	public void shouldQueryDataWithStringQuery() {
//		StepVerifier.create(repository.findByFirstnameInAndLastname("Walter", "White")).expectNextCount(1).verifyComplete();
//	}
//
//	/**
//	 * Fetch data using query derivation.
//	 */
//	@Test
//	public void shouldQueryDataWithDeferredQueryDerivation() {
//		StepVerifier.create(repository.findByLastname(Mono.just("White"))).expectNextCount(2).verifyComplete();
//	}
//
//	/**
//	 * Fetch data using query derivation and deferred parameter resolution.
//	 */
//	@Test
//	public void shouldQueryDataWithMixedDeferredQueryDerivation() {
//
//		StepVerifier.create(repository.findByFirstnameAndLastname(Mono.just("Walter"), "White")) //
//				.expectNextCount(1) //
//				.verifyComplete();
//	}

}
