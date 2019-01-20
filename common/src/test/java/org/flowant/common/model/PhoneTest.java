package org.flowant.common.model;

import java.util.Locale;

import org.flowant.common.model.Phone;
import org.junit.Assert;
import org.junit.Test;

import com.google.i18n.phonenumbers.NumberParseException;


public class PhoneTest {
    @Test
    public void testParse() throws NumberParseException {
        Phone p = Phone.parse("+821050671678", Locale.US.getCountry());
        Assert.assertEquals(82, p.getCountryCode());
        Assert.assertEquals(1050671678L, p.getNationalNumber());
    }

    @Test
    public void testParseDefaultCountry() throws NumberParseException {
        Phone p = Phone.parse("1050671678", Locale.US.getCountry());
        Assert.assertEquals(1, p.getCountryCode());
        Assert.assertEquals(1050671678L, p.getNationalNumber());
    }
}
