package org.motechproject.common.domain;


import org.apache.commons.lang.StringUtils;
import org.motechproject.common.exception.PhoneNumberFormatException;

public class PhoneNumber {
    private Long phoneNumber;


    public PhoneNumber(String phoneNumber) throws PhoneNumberFormatException {
        if (isNotValid(phoneNumber)) throw new PhoneNumberFormatException();
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public static boolean isValid(String phoneNumber) {
        return !isNotValid(phoneNumber);
    }

    public static boolean isNotValid(String phoneNumber) {
        return StringUtils.isBlank(phoneNumber) ||
                phoneNumber.length() < 10 ||
                !StringUtils.isNumeric(phoneNumber) ||
                (phoneNumber.length() == 12 && !(phoneNumber.startsWith("91") || phoneNumber.startsWith("00"))) ||
                (phoneNumber.length() > 10 && phoneNumber.length() != 12);
    }

    private Long formatPhoneNumber(String phoneNumber) {
        Long returnValue = null;
        if (phoneNumber.length() == 10)
            returnValue = Long.parseLong("91" + phoneNumber);
        if (phoneNumber.length() == 12 && (phoneNumber.startsWith("91") || phoneNumber.startsWith("00")))
            returnValue = Long.parseLong("91" + phoneNumber.substring(2, 12));
        return returnValue;
    }
}
