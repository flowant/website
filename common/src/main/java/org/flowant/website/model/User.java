package org.flowant.website.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class User implements UserDetails, HasIdentity, HasCruTime {
    private static final long serialVersionUID = 3027599136204429983L;

    @NonNull
    @Id
    @Column("id")
    UUID identity;

    @NonNull
    @Indexed //TODO username should be unique
    @Column("un")
    String username;

    @NonNull
    @Column("pw")
    String password;

    @NonNull
    @Column("e")
    String email;

    @Column("fn")
    String firstname;

    @Column("ln")
    String lastname;

    @Builder.Default
    @Column("g")
    Gender gender = Gender.UNDEFINED;

    @Column("bd")
    ZonedDate birthdate;

    @Column("p")
    Phone phone;

    @Column("ad")
    PostalAddress address;

    @Column("at")
    List<Authority> authorities;

    @Column("fe")
    Set<UUID> followers;

    @Column("fw")
    Set<UUID> followings;

    // TODO likes and dislikes: contents, Reviews, Replys
    @Column("it")
    Set<String> interests; //TODO to be updated by user activities;

    @NonNull
    @Column("ct")
    CRUZonedTime cruTime;//TODO updated time

    @Builder.Default
    @Column("ane")
    boolean accountNonExpired = true;

    @Builder.Default
    @Column("anl")
    boolean accountNonLocked = true;;

    @Builder.Default
    @Column("cne")
    boolean credentialsNonExpired = true;

    @Builder.Default
    @Column("en")
    boolean enabled = true;

}
