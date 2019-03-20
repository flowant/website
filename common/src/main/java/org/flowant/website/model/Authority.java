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

    public static final String CEO = "CEO";
    public static final String CTO = "CTO";
    public static final String MANAGER = "MANAGER";
    public static final String MEMBER = "MEMBER";
    public static final String GUEST = "GUEST";

    private static final long serialVersionUID = -4710371112263760289L;

    @NonNull
    String authority;

}
