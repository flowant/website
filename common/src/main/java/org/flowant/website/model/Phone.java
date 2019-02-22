package org.flowant.website.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType
public class Phone {

    int countryCode;

    long nationalNumber;

    public static Phone of(int countryCode, long nationalNumber) {
        return new Phone(countryCode, nationalNumber);
    }

    public static Phone parse(CharSequence numberToParse, String defaultCountry) throws NumberParseException {
        PhoneNumber number = PhoneNumberUtil.getInstance().parse(numberToParse, defaultCountry);
        return Phone.of(number.getCountryCode(), number.getNationalNumber());
    }

}
