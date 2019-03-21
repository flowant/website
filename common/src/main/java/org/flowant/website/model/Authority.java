package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Authority implements GrantedAuthority {

    public static final String ADMIN = "A";
    public static final String USER = "U";
    public static final String GUEST = "G";

    private static final long serialVersionUID = -4710371112263760289L;

    @NonNull
    String authority;

}
