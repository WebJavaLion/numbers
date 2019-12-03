package ru.numbers.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.service.ContactService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lev_S
 */

@RestController
@RequestMapping("/contacts")
@Transactional
public class ContactController {

    @Autowired
    ContactService service;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Contact> contactOptional = service.getById(id);

        if (contactOptional.isPresent()) {
            return ResponseEntity.ok(contactOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public List<Contact> getAll() {
        return service.getAll();
    }

    @GetMapping("/numbers/{number}")
    public List<Contact> getContactByNumber(@PathVariable String number) {
        return service.getContactByNumber(number);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @RequestBody @Valid Contact contact,
                       BindingResult bindingResult) {

        service.updateContact(id, contact, bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getField)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        return service.deleteById(id)? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
