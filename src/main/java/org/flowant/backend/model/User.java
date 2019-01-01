package org.flowant.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;
import lombok.experimental.Delegate;
import lombok.NonNull;

@Data(staticConstructor = "of")
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
    Gender gender = Gender.UNDEFINED;
    ZonedDate birthdate;
    Phone phone;
    PostalAddress address;
    Authority role = Authority.ANONYMOUS;
    List<UUID> followers;
    List<UUID> following;
    List<Tag> interest; //TODO to be updated by user activities;
    @NonNull
    CRUDZonedTime crudTime;//TODO updated time
}
