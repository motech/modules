package org.motechproject.common.domain;


import org.apache.commons.lang.StringUtils;
import org.motechproject.common.exception.PhoneNumberFormatException;

public class PhoneNumber {
    private String phoneNumber;


    public PhoneNumber(String phoneNumber) throws PhoneNumberFormatException {
        if (isNotValid(phoneNumber)) throw new PhoneNumberFormatException();
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }

    public static boolean isValid(String phoneNumber) {
        return !isNotValid(phoneNumber);
    }

    private static boolean isNotValid(String phoneNumber) {
        return StringUtils.isBlank(phoneNumber) ||
                phoneNumber.length() < 10 ||
                !StringUtils.isNumeric(phoneNumber) ||
                (phoneNumber.length() == 12 && !(phoneNumber.startsWith("91") || phoneNumber.startsWith("00"))) ||
                (phoneNumber.length() > 10 && phoneNumber.length() != 12);
    }

    private String formatPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) return StringUtils.EMPTY;
        if (phoneNumber.length() == 10) return "91" + phoneNumber;
        if (phoneNumber.length() == 12 && (phoneNumber.startsWith("91") || phoneNumber.startsWith("00")))
            return "91" + phoneNumber.substring(2, 12);
        return StringUtils.EMPTY;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
