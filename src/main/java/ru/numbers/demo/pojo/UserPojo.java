package ru.numbers.demo.pojo;

import lombok.*;
import ru.numbers.demo.entitiy.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Lev_S
 */
@Getter
@Setter
@AllArgsConstructor
public class UserPojo extends User {

    @NotBlank
    private String updatedName;

    @NotNull
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
    private String updatedPassword;

    @NotNull
    @Email
    private String updatedEmail;

    public UserPojo(User user) {
        super(user);
    }


}
