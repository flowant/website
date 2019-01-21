package org.flowant.website.util.test;

import java.util.List;
import java.util.UUID;

import org.flowant.website.model.Authority;
import org.flowant.website.model.CRUZonedTime;
import org.flowant.website.model.Phone;
import org.flowant.website.model.PostalAddress;
import org.flowant.website.model.Tag;
import org.flowant.website.model.User;
import org.flowant.website.model.ZonedDate;
import org.junit.Assert;
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
    static String name = "name";

    public static User small(int suffix) {
        return User.builder().id(UUID.randomUUID()).username(username + suffix).password(password + suffix)
                .email(email + suffix).cruTime(CRUZonedTime.now()).build();
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
                .phone(Phone.of(suffix, 100000000 + suffix)).followers(List.of(UUID.randomUUID()))
                .authorities(List.of(Authority.of("USER"), Authority.of("MEMBER")))
                .followings(List.of(UUID.randomUUID())).interests(List.of(Tag.of(name + suffix)))
                .cruTime(CRUZonedTime.now()).build();
    }

    public static User large() {
        return large(0);
    }

    public static User assertEqual(User expected, User actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getFirstname(), actual.getFirstname());
        Assert.assertEquals(expected.getLastname(), actual.getLastname());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getBirthdate(), actual.getBirthdate());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
        Assert.assertEquals(expected.getGender(), actual.getGender());
        AssertUtil.assertListEquals(expected.getAuthorities(), actual.getAuthorities());
        AssertUtil.assertListEquals(expected.getFollowers(), actual.getFollowers());
        AssertUtil.assertListEquals(expected.getFollowings(), actual.getFollowings());
        AssertUtil.assertListEquals(expected.getInterests(), actual.getInterests());
        return actual;
    }

    @Test
    public void testMaker() {
        log.debug("User:{}", small()::toString);
        log.debug("User:{}", large()::toString);
    }

}
