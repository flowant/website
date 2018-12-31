package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data(staticConstructor = "of")
@UserDefinedType
public class PostalAddress {
    String address;
    /* delivery address line can be parsed as follows
    String primaryNumber;
    String direction;
    String street;
    String streetSuffix;
    String unit;
    */
    String city;
    String state;
    String country;
    String zipCode;
    String detailCode; /* e.g., +4 zip code */
}
