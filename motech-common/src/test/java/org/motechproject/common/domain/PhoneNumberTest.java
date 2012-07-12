package org.motechproject.common.domain;

import org.junit.Test;

import static junit.framework.Assert.*;

public class PhoneNumberTest {
    @Test
    public void shouldFailValidationForNullOrEmpty() {
        assertFalse(PhoneNumber.isValid(""));
        assertFalse(PhoneNumber.isValid(null));
    }

    @Test
    public void shouldFailValidationForPhoneNumberLessThanOrGreaterThan10Digits() {
        assertFalse(PhoneNumber.isValid("12"));
        assertFalse(PhoneNumber.isValid("123456789012312"));
    }

    @Test
    public void shouldFailValidationIfTheNumberContainsInvalidCharacters() {
        assertFalse(PhoneNumber.isValid("test1123123-"));
    }

    @Test
    public void shouldPassValidationIfPhoneNumberIs12DigitButStartsWith00Or91() {
        assertTrue(PhoneNumber.isValid("911234567890"));
        assertTrue(PhoneNumber.isValid("001234567890"));
    }

    @Test
    public void shouldPassValidationFor10DigitPhoneNumber() {
        assertTrue(PhoneNumber.isValid("1234567890"));
    }

    @Test
    public void shouldReturn10DigitPhoneNumberWhenGivenA10DigitNumber() {
        String phoneNumber = "1234567890";
        assertEquals(Long.valueOf(phoneNumber), PhoneNumber.formatPhoneNumberTo10Digits(phoneNumber));
    }

    @Test
    public void shouldReturn10DigitPhoneNumberWhenGivenA12DigitNumberStartingWith91Or00() {
        String phoneNumber1 = "911234567890";
        Long expectedNumber = Long.parseLong("1234567890");
        assertEquals(expectedNumber, PhoneNumber.formatPhoneNumberTo10Digits(phoneNumber1));

        String phoneNumber2 = "001234567890";
        assertEquals(expectedNumber, PhoneNumber.formatPhoneNumberTo10Digits(phoneNumber2));
    }

    @Test
    public void shouldNotReturn10DigitPhoneNumberWhenGivenAnInvalidNumber() {
        String phoneNumber1 = "01234567890";
        assertNull(PhoneNumber.formatPhoneNumberTo10Digits(phoneNumber1));
    }

    @Test
    public void shouldAppend91To12DigitPhoneNumbersAnd10DigitPhoneNumbers() {
        PhoneNumber phoneNumber = new PhoneNumber("1234567890");
        assertEquals(new Long(911234567890L), phoneNumber.getPhoneNumber());

        phoneNumber = new PhoneNumber("001234567890");
        assertEquals(new Long(911234567890L), phoneNumber.getPhoneNumber());
    }

    @Test
    public void shouldReturnNullIfNumberIsNotInTheRightFormat() {
        PhoneNumber invalidPhoneNumber = new PhoneNumber("invalidPhoneNumber");
        assertNull(invalidPhoneNumber.getPhoneNumber());
    }

    @Test
    public void shouldPassValidationForBlanks() {
        assertTrue(PhoneNumber.isValidWithBlanksAllowed(null));
        assertTrue(PhoneNumber.isValidWithBlanksAllowed(""));
    }
}
