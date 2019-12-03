package ru.numbers.demo.enums;

/**
 * @author Lev_S
 */

public enum ValidateUserErrorEnum {

    INVALID_USER_EMAIL("Such email does not exist."),

    INVALID_USER_PASSWORD("Password must be at least 8 characters long, contain" +
            "at least one lowercase and uppercase letter and at least one special character (@ / $ /%, etc.)."),

    INVALID_USER_NAME("Name shouldn't be empty"),
    DEFAULT_ERROR("Validation exception");

    private String value;

    ValidateUserErrorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
