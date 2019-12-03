package ru.numbers.demo.service;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.entitiy.User;
import ru.numbers.demo.enums.ValidateUserErrorEnum;
import ru.numbers.demo.exception.ValidateException;
import ru.numbers.demo.repository.ContactRepository;
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
    ContactRepository contactRepository;

    @MockBean
    UserRepository userRepository;

    @Test
    void successCreation() {

        List<String> errors = new ArrayList<>();
        User user = new User();
        user.setEmail("example@test.com");

        Mockito.doReturn(Optional.empty()).when(userRepository).findUserByEmail(user.getEmail());

        assertDoesNotThrow(() -> userService.createUser(user, errors));
        assertNotNull(user.getPhoneBook());
        assertEquals(user, user.getPhoneBook().getUser());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void unSuccessfulCreationOfEmailAlreadyExists() {
        List<String> errors = new ArrayList<>();
        User user = new User();

        Mockito.doReturn(Optional.of(new User())).when(userRepository).findUserByEmail(user.getEmail());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(user, errors)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("400 BAD_REQUEST \"user with such email already exists\"", exception.getMessage());
    }

    @Test
    void unSuccessfulCreationOfBadRequest() {
        List<String> errors = new ArrayList<>();
        errors.add("some field error");

        User user = new User();
        Mockito.doReturn(Optional.empty()).when(userRepository).findUserByEmail(user.getEmail());

        ValidateException validateException = assertThrows(ValidateException.class,
                () -> userService.createUser(user, errors)
        );

        assertEquals(ValidateUserErrorEnum.DEFAULT_ERROR.getValue(), validateException.getMessage());
    }

    @Test
    void deleteIfUserExists() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(new User()));

        assertTrue(userService.deleteUserById(user.getId()));

        Mockito.verify(userRepository, Mockito.times(1)).deleteUserById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void deleteIfUserDoesNotExist() {
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertFalse(userService.deleteUserById(user.getId()));

        Mockito.verify(userRepository, Mockito.never()).deleteUserById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    void successUserUpdate() {
        User user = new User();
        user.setId(1L);
        user.setEmail("example@test.com");
        User userSpy = Mockito.spy(user);

        List<String> errors = new ArrayList<>();

        Mockito.when(userRepository.findUserByEmail(userSpy.getEmail()))
                .thenReturn(Optional.empty());

        Mockito.when(userRepository.findById(userSpy.getId()))
                .thenReturn(Optional.of(userSpy));

        assertDoesNotThrow(() ->
                userService.updateUser(
                        userSpy.getId(),
                        userSpy, errors
                )
        );

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(userSpy, Mockito.times(1))
                .setPhoneBook(
                        ArgumentMatchers
                                .any()
                );
    }

    @Test
    void unSuccessUpdate() {
        User user = new User();
        List<String> errors = new ArrayList<>();
        user.setId(1L);
        user.setEmail("example@test.com");
        User userSpy = Mockito.spy(user);
        userSpy.setEmail("example@test.com");

        Mockito.when(userRepository.findUserByEmail("example@test.com")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException emailException = assertThrows(
                ResponseStatusException.class, () -> userService.updateUser(2L, userSpy, errors));

        Mockito.verify(userSpy, Mockito.never()).setId(Mockito.anyLong());
        Mockito.verify(userSpy, Mockito.never()).setPhoneBook(Mockito.any());

        assertEquals("400 BAD_REQUEST \"User with such email already exists\"", emailException.getMessage());

        Mockito.when(userRepository.findUserByEmail("example@test.com")).thenReturn(Optional.empty());

        ResponseStatusException idException = assertThrows
                (ResponseStatusException.class, () -> userService.updateUser(2L, userSpy, errors));

        assertEquals("404 NOT_FOUND \"User with such Id doesn't exist\"", idException.getMessage());
    }

    @Test
    void addContactForUser() {
        User user = new User();
        user.setId(1L);
        user.setPhoneBook(new PhoneBook());
        List<String> errors = new ArrayList<>();
        Contact contact = new Contact();
        Contact contactSpy = Mockito.spy(contact);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.createNewContact(1L, contactSpy, errors));

        Mockito.verify(contactRepository, Mockito.times(1)).save(contactSpy);
        Mockito.verify(contactSpy, Mockito.times(1)).setPhoneBook(user.getPhoneBook());
    }

    @Test
    void failAddContact(){
        User user = new User();
        user.setPhoneBook(new PhoneBook());
        List<String> errors = new ArrayList<>();
        Contact contact = new Contact();
        Contact contactSpy = Mockito.spy(contact);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> userService.createNewContact(1L, contactSpy, errors)
        );
        Mockito.verify(contactRepository, Mockito.never()).save(contactSpy);
    }
}
