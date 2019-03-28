package org.flowant.website.util.test;

import java.util.Set;
import java.util.UUID;

import org.flowant.website.model.Authority;
import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Phone;
import org.flowant.website.model.PostalAddress;
import org.flowant.website.model.User;
import org.flowant.website.model.ZonedDate;
import org.flowant.website.util.IdMaker;

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

    static String tag = "tag";

    public static User small(UUID id) {
        return User.builder()
                .identity(id)
                .username(username + id.toString().substring(0, 8))
                .password(password + id)
                .email(email + id)
                .authorities(Set.of(Authority.of("ROLE_USER"), Authority.of(Authority.USER)))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static User smallRandom() {
        return small(IdMaker.randomUUID());
    }

    public static User large(UUID id) {
        return User.builder()
                .identity(id)
                .username(username + id.toString().substring(0, 8))
                .password(password + id)
                .email(email + id)
                .postalAddress(PostalAddress.of(address + id, city + id, state + id, country + id, zipCode + id))
                .birthdate(ZonedDate.now())
                .firstname(firstname + id)
                .lastname(lastname + id)
                .phone(Phone.of(82, id.hashCode()))
                .authorities(Set.of(Authority.of("ROLE_USER"), Authority.of(Authority.USER)))
                .interests(Set.of(tag + id))
                .cruTime(CRUZonedTime.now())
                .build();
    }

    public static User largeRandom() {
        return large(IdMaker.randomUUID());
    }

}
