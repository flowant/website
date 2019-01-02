package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
@UserDefinedType
public class PostalAddress {
    @NonNull
    String address;
    /* delivery address line can be parsed as follows
    String primaryNumber;
    String direction;
    String street;
    String streetSuffix;
    String unit;
    */
    @NonNull
    String city;
    @NonNull
    String state;
    @NonNull
    String country;
    @NonNull
    String zipCode;
    String detailCode; /* e.g., +4 zip code */
}
