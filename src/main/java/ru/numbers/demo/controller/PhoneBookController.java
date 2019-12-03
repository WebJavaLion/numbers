package ru.numbers.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.service.PhoneBookService;

import java.util.List;
import java.util.Optional;

/**
 * @author Lev_S
 */
@RestController
@RequestMapping("/books")
@Transactional
public class PhoneBookController {

    @Autowired
    PhoneBookService service;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<PhoneBook> phoneBookOptional = service.getById(id);

        if (phoneBookOptional.isPresent()) {
            return ResponseEntity.ok(phoneBookOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping()
    public List<PhoneBook> getAll() {
        return service.getAll();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getByUserId(@PathVariable Long userId) {
        Optional<PhoneBook> phoneBookOptional = service.getByUserId(userId);

        if (phoneBookOptional.isPresent()) {
            return ResponseEntity.ok(phoneBookOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
