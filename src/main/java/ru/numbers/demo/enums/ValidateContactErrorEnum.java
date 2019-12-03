package ru.numbers.demo.enums;

import lombok.Getter;

/**
 * @author Lev_S
 */
@Getter
public enum ValidateContactErrorEnum {

    INVALID_CONTACT_NUMBER("Phone number should contain only numerals or '+' and can't be empty, max length 12."),
    INVALID_CONTACT_NAME("Name shouldn't be empty.");

    private String value;

    ValidateContactErrorEnum(String value) {
        this.value = value;
    }
}
