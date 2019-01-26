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

    public static User small(UUID id) {
        return User.builder().id(id).username(username + id).password(password + id)
                .email(email + id).cruTime(CRUZonedTime.now()).build();
    }

    public static User smallRandom() {
        return small(UUID.randomUUID());
    }

    public static User large(UUID id) {
        return User.builder().id(id).username(username + id).password(password + id)
                .email(email + id)
                .address(PostalAddress.of(address + id, city + id, state + id, country + id,
                        zipCode + id))
                .birthdate(ZonedDate.now()).firstname(firstname + id).lastname(lastname + id)
                .phone(Phone.of(82, id.hashCode())).followers(List.of(UUID.randomUUID()))
                .authorities(List.of(Authority.of("ROLE_USER"), Authority.of("MEMBER")))
                .followings(List.of(UUID.randomUUID())).interests(List.of(Tag.of(name + id)))
                .cruTime(CRUZonedTime.now()).build();
    }

    public static User largeRandom() {
        return large(UUID.randomUUID());
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

}
