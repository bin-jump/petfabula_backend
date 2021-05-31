package com.petfabula.domain.common.validation;

import com.petfabula.domain.exception.InvalidValueException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;

public abstract class EntityValidationUtils {

    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    private static final RegexValidator userNameValidator = new RegexValidator("^[\\.\\w\\-ぁ-んァ-ヶ一-龠々ー]{3,20}$");

    private static final RegexValidator passwordValidator = new RegexValidator("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]){8,16}$");

    private static final RegexValidator petNameValidator = new RegexValidator("^[\\.\\w\\-ぁ-んァ-ヶ一-龠々ー]{1,16}$");


    public static void notEmpty(String fieldName, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new InvalidValueException(fieldName, MessageKey.EMPTY_STRING);
        }
    }


    public static void validEmail(String fieldName, String value) {
        if (!emailValidator.isValid(value)) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_MAIL);
        }
        if (StringUtils.length(value) > 120) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_MAIL);
        }
    }

    public static void validUserName(String fieldName, String value) {
        if (StringUtils.length(value) < 3 || StringUtils.length(value) > 20) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_USER_NAME_LENGTH);
        }
        if (!userNameValidator.isValid(value)) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_USER_NAME_PATTERN);
        }
    }

    public static void validPetName(String fieldName, String value) {
        if (StringUtils.length(value) < 1 || StringUtils.length(value) > 16) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_PET_NAME_LENGTH);
        }
        if (!petNameValidator.isValid(value)) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_PET_NAME_PATTERN);
        }
    }

    public static void validPassword(String fieldName, String value) {
        if (StringUtils.length(value) < 8 || StringUtils.length(value) > 16) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_PASSWORD);
        }
        if (!passwordValidator.isValid(value)) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_PASSWORD);
        }
    }

    public static void validStringLendth(String fieldName, String value, int min, int max) {
        if (StringUtils.length(value) < min || StringUtils.length(value) > max) {
            throw new InvalidValueException(fieldName, MessageKey.INVALID_STRING_LEGNTH);
        }
    }
}
