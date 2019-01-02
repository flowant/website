package org.flowant.backend.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

@Data
@Builder
@Table
public class User {
    @Id
    @NonNull
    UUID id;
    @NonNull
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
    @Builder.Default
    Authority role = Authority.ANONYMOUS;
    @Singular
    List<UUID> followers;
    @Singular
    List<UUID> followings;
    @Singular
    List<Tag> interests; //TODO to be updated by user activities;
    @NonNull
    CRUDZonedTime crudTime;//TODO updated time
}
