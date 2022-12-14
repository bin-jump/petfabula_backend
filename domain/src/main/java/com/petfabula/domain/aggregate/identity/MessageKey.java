package com.petfabula.domain.aggregate.identity;

public final class MessageKey {

    private static final String PREFIX = "identity.";

    public static final String NOT_REGISTERED = PREFIX + "notRegistered";

    public static final String USER_NAME_ALREADY_EXISTS = PREFIX + "userNameAlreadyExists";

    public static final String EMAIL_ALREADY_REGISTERED = PREFIX + "emailAlreadyRegistered";

    public static final String INVALID_VERIFICATION_CODE = PREFIX + "invalidVerificationCode";

    public static final String WRONG_EMAIL_OR_PASSWORD = PREFIX + "wrongEmailOrPassword";

    public static final String EMAIL_NOT_REGISTERED = PREFIX + "emailNotRegister";

    public static final String ROLE_NOT_EXISTS = PREFIX + "roleNotExists";
}
