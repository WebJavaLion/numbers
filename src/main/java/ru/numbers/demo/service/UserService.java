package ru.numbers.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.entitiy.User;
import ru.numbers.demo.exception.ValidateException;
import ru.numbers.demo.repository.ContactRepository;
import ru.numbers.demo.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Lev_S
 */

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ContactRepository contactRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user, List<String> errors) {

        validateUser(user, errors);
        return userRepository.save(user);
    }

    private void validateFields(List<String> errors) {

        if (!errors.isEmpty()) {
            throw new ValidateException(errors);
        }
    }

    private void validateUser(User user, List<String> errors) {

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with such email already exists");
        }

        validateFields(errors);

        PhoneBook phoneBook = new PhoneBook();

        phoneBook.setDateOfCreation(new Timestamp(new Date().getTime()));
        phoneBook.setUser(user);

        user.setPhoneBook(phoneBook);
    }

    public boolean deleteUserById(Long id) {

        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteUserById(id);
            return true;
        } else {
            return false;
        }
    }

    public void updateUser(Long id, User user, List<String> errors) {

        validateFields(errors);

        Optional<User> userPresent = userRepository.findUserByEmail(user.getEmail());
        if (userPresent.isPresent() && !userPresent.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such email already exists");
        }

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            user.setId(userOptional.get().getId());
            user.setPhoneBook(userOptional.get().getPhoneBook());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such Id doesn't exist");
        }

        userRepository.save(user);
    }

    public List<User> findByName(String name) {
        return userRepository.findUsersByNameIsContaining(name, Sort.by("name").ascending());
    }

    public Contact createNewContact(Long id, Contact contact, List<String> errors) {
        validateFields(errors);

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            contact.setPhoneBook(userOptional.get().getPhoneBook());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with such Id doesn't exists");
        }
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContactsForUser(Long id) {
        List<Contact> contactList = new ArrayList<>();

        userRepository.findById(id).ifPresent(user -> contactList.addAll(user.getPhoneBook().getContacts()));
        return contactList;
    }
}
