package org.flowant.backend.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.Data;

@Data(staticConstructor = "of")
@UserDefinedType
public class Phone {
    final int countryCode;
    final long nationalNumber;

    public static Phone parse(CharSequence numberToParse, String defaultCountry) throws NumberParseException {
        PhoneNumber number = PhoneNumberUtil.getInstance().parse(numberToParse, defaultCountry);
        return Phone.of(number.getCountryCode(), number.getNationalNumber());
    }
}
