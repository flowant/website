package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
@UserDefinedType
public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = -4710371112263760289L;
    @NonNull
    String authority;
}
