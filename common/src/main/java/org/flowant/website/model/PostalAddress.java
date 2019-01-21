package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor(staticName="of")
@NoArgsConstructor
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
