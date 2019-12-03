package ru.numbers.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.entitiy.User;
import ru.numbers.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lev_S
 */

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {

        Optional<User> userOptional = service.getUserById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/names/{name}")
    public List<User> getUserByNameLike(@PathVariable String name) {
        return service.findByName(name);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid User user, BindingResult bindingResult) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.createUser(user, bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getField)
                        .collect(Collectors.toList())
                ).getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
       return service.deleteUserById(id) ?
               ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id,
                           @RequestBody @Valid User user,
                           BindingResult bindingResult) {

        service.updateUser(id, user, bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.toList()));
    }

    @GetMapping("{id}/contacts")
    public List<Contact> getAllContacts(@PathVariable Long id) {
        return service.getAllContactsForUser(id);
    }

    @PostMapping("{id}/contacts")
    public ResponseEntity createContact(@PathVariable Long id,
                                        @RequestBody @Valid Contact contact,
                                        BindingResult bindingResult) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                service.createNewContact(id, contact, bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getField)
                        .collect(Collectors.toList()
                        )
                ).getId());
    }
}
