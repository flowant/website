package org.flowant.backend.model;

import java.util.UUID;

import org.junit.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserMaker {

    static String username = "username";
    static String password = "password";
    static String email = "email";
    static String firstname = "firstname";
    static String lastname = "lastname";

    // PostalAddress
    static String address = "address";
    static String city = "city";
    static String state = "state";
    static String country = "country";
    static String zipCode = "zipCode";
    static String detailCode = "detailCode";

    // Tag
    static String tag = "tag";

    public static User small(int suffix) {
        return User.builder().id(UUID.randomUUID()).username(username + suffix).password(password + suffix)
                .email(email + suffix).crudTime(CRUDZonedTime.now()).build();
    }

    public static User small() {
        return small(0);
    }

    public static User large(int suffix) {
        return User.builder().id(UUID.randomUUID()).username(username + suffix).password(password + suffix)
                .email(email + suffix)
                .address(PostalAddress.of(address + suffix, city + suffix, state + suffix, country + suffix,
                        zipCode + suffix))
                .birthdate(ZonedDate.now()).firstname(firstname + suffix).lastname(lastname + suffix)
                .phone(Phone.of(suffix, 100000000 + suffix)).follower(UUID.randomUUID()).following(UUID.randomUUID())
                .interest(Tag.of(tag + suffix))
                .crudTime(CRUDZonedTime.now()).build();
    }

    public static User large() {
        return large(0);
    }

    @Test
    public void make() {
        log.info("User:{}", small()::toString);
        log.debug("User:{}", large()::toString);
    }
}
