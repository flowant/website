package org.flowant.website.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@Table
public class User implements UserDetails, Model {
    private static final long serialVersionUID = 3027599136204429983L;

    @Id @NonNull
    UUID id;
    @NonNull @Indexed //TODO username should be unique
    String username;
    @NonNull
    String password;
    @NonNull
    String email;
    String firstname;
    String lastname;
    @Builder.Default
    Gender gender = Gender.UNDEFINED;
    ZonedDate birthdate;
    Phone phone;
    PostalAddress address;
    List<Authority> authorities;
    List<UUID> followers;
    List<UUID> followings;
    List<Tag> interests; //TODO to be updated by user activities;
    @NonNull
    CRUZonedTime cruTime;//TODO updated time
    @Builder.Default
    boolean accountNonExpired = true;
    @Builder.Default
    boolean accountNonLocked = true;;
    @Builder.Default
    boolean credentialsNonExpired = true;
    @Builder.Default
    boolean enabled = true;
}
