package ru.numbers.demo;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.entitiy.User;
import ru.numbers.demo.enums.ValidateUserErrorEnum;
import ru.numbers.demo.exception.ValidateException;
import ru.numbers.demo.repository.UserRepository;
import ru.numbers.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository repository;

    @Test
    void successCreation() {

        List<String> errors = new ArrayList<>();
        User user = new User();
        user.setEmail("example@test.com");

        Mockito.doReturn(Optional.empty()).when(repository).findUserByEmail(user.getEmail());

        assertDoesNotThrow(() -> userService.createUser(user, errors));
        assertNotNull(user.getPhoneBook());
        assertEquals(user, user.getPhoneBook().getUser());

        Mockito.verify(repository, Mockito.times(1)).save(user);
    }

    @Test
    void unSuccessfulCreationOfEmailAlreadyExists() {
        List<String> errors = new ArrayList<>();
        User user = new User();

        Mockito.doReturn(Optional.of(new User())).when(repository).findUserByEmail(user.getEmail());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(user, errors)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("404 NOT_FOUND \"user with such email already exists\"", exception.getMessage());
    }

    @Test
    void unSuccessfulCreationOfBadRequest() {
        List<String> errors = new ArrayList<>();
        errors.add("some field error");

        User user = new User();
        Mockito.doReturn(Optional.empty()).when(repository).findUserByEmail(user.getEmail());

        ValidateException validateException = assertThrows(ValidateException.class,
                () -> userService.createUser(user, errors)
        );

        assertEquals(ValidateUserErrorEnum.DEFAULT_ERROR.getValue(), validateException.getMessage());
    }

    @Test
    void deleteIfUserExists() {
        User user = new User();
        user.setId(1L);
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(new User()));

        assertTrue(userService.deleteUserById(user.getId()));

        Mockito.verify(repository, Mockito.times(1)).deleteUserById(user.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(user.getId());

    }

    @Test
    void deleteIfUserDoesNotExist() {
        User user = new User();
        user.setId(1L);
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.empty());

        assertFalse(userService.deleteUserById(user.getId()));

        Mockito.verify(repository, Mockito.never()).deleteUserById(user.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void successUserUpdate() {
        User user = new User();
        user.setId(1L);
        user.setEmail("example@test.com");
        User userSpy = Mockito.spy(user);

        List<String> errors = new ArrayList<>();

        Mockito.when(repository.findUserByEmail(userSpy.getEmail()))
                .thenReturn(Optional.empty());

        Mockito.when(repository.findById(userSpy.getId()))
                .thenReturn(Optional.of(userSpy));

        assertDoesNotThrow(() ->
                userService.updateUser(
                        userSpy.getId(),
                        userSpy, errors
                )
        );

        Mockito.verify(repository, Mockito.times(1)).save(user);
        Mockito.verify(userSpy, Mockito.times(1))
                .setPhoneBook(
                        ArgumentMatchers
                                .any()
                );
    }
}
