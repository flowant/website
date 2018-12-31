package org.flowant.users.data;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@UserDefinedType
public class Phone {
    int countryCode;
    long nationalNumber;

    public static Phone parse(CharSequence numberToParse, String defaultCountry) throws NumberParseException {
        PhoneNumber number = PhoneNumberUtil.getInstance().parse(numberToParse, defaultCountry);
        return new Phone(number.getCountryCode(), number.getNationalNumber());
    }
}
