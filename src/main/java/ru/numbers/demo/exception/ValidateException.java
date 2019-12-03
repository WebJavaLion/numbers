package ru.numbers.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.numbers.demo.enums.ValidateContactErrorEnum;
import ru.numbers.demo.enums.ValidateUserErrorEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lev_S
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidateException extends RuntimeException {

    private List<String> errorList;

    public ValidateException(List<String> errorList) {
        this.errorList = errorList;
    }

    public ValidateException(String message) {
        super(message);
        this.errorList = new ArrayList<>();
        errorList.add(message);
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();

        outerLoop:
        for (String value : errorList) {

            switch (value) {
                case "email":
                    builder.append(ValidateUserErrorEnum.INVALID_USER_EMAIL.getValue());
                    break;
                case "password":
                    builder.append(ValidateUserErrorEnum.INVALID_USER_PASSWORD.getValue());
                    break;
                case "name":
                    builder.append(ValidateUserErrorEnum.INVALID_USER_NAME.getValue());
                    break;
                case "contactName":
                    builder.append(ValidateContactErrorEnum.INVALID_CONTACT_NAME.getValue());
                    break;
                case "number":
                    builder.append(ValidateContactErrorEnum.INVALID_CONTACT_NUMBER.getValue());
                    break;
                default:
                    builder.delete(0, builder.length());
                    builder.append(ValidateUserErrorEnum.DEFAULT_ERROR.getValue());
                    break outerLoop;
            }
        }
        return builder.toString();
    }
}
