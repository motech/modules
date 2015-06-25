package org.motechproject.openmrs19.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Random password generator. It generates a random passwords of length specified in the constructor, built from
 * letters (lower and upper case) and numbers. It's used for generating passwords for new users and users that were
 * modified.
 */
public class Password {

    private static final Logger LOGGER = LoggerFactory.getLogger(Password.class);

    private static final int NUMBER_OF_NUMERIC_CHARS_TO_APPEND = 3;
    private Integer length;

    private static final char[] PASSWORD_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0'};
    private static final char[] PASSWORD_LOWER_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z'};

    private static final char[] PASSWORD_UPPER_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'};
    private static final char[] PASSWORD_NUMBER_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * Creates a generator that generates passwords of length {@code length}.
     *
     * @param length  the length of the password to generate
     */
    public Password(Integer length) {
        this.length = length;
    }

    /**
     * Generates a random password of length specified in the constructor.
     *
     * @return the generated password
     */
    public String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - NUMBER_OF_NUMERIC_CHARS_TO_APPEND; i++) {
            sb.append(PASSWORD_CHARS[((int) (Math.random() * PASSWORD_CHARS.length))]);
        }
        sb.append(PASSWORD_NUMBER_CHARS[((int) (Math.random() * PASSWORD_NUMBER_CHARS.length))]);
        sb.append(PASSWORD_LOWER_CHARS[((int) (Math.random() * PASSWORD_NUMBER_CHARS.length))]);
        sb.append(PASSWORD_UPPER_CHARS[((int) (Math.random() * PASSWORD_NUMBER_CHARS.length))]);
        LOGGER.info("password: " + sb.toString());
        return sb.toString();
    }

}
