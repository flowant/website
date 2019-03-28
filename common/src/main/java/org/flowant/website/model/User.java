package org.flowant.website.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
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
    @PrimaryKey("id")
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

    @Column("dn")
    String displayName;

    @Builder.Default
    @Column("g")
    Gender gender = Gender.U;

    @Column("bd")
    ZonedDate birthdate;

    @Column("p")
    Phone phone;

    @Column("ad")
    PostalAddress postalAddress;

    @NonNull
    @Column("at")
    Set<Authority> authorities;

    @Column("l")
    Set<UUID> likes; //TODO to be updated by user activities;

    @Column("it")
    Set<String> interests; //TODO to be updated by user activities;

    @Column("fr")
    List<FileRef> fileRefs;

    @NonNull
    @Column("ct")
    CRUZonedTime cruTime;//TODO updated time

    @Builder.Default
    @Column("ane")
    boolean accountNonExpired = true;

    @Builder.Default
    @Column("anl")
    boolean accountNonLocked = true;

    @Builder.Default
    @Column("cne")
    boolean credentialsNonExpired = true;

    @Builder.Default
    @Column("en")
    boolean enabled = true;

    public String getDisplayName() {
        if(displayName == null) {
            displayName = username;
        }
        return displayName;
    }
}
