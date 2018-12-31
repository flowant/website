package org.flowant.users.data;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;
import lombok.NonNull;

@Data
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
    CRUDZonedTime crudTime = CRUDZonedTime.now();//TODO updated time
}
