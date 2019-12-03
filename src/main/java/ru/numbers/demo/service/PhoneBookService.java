package ru.numbers.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.repository.PhoneBookRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Lev_S
 */
@Service
public class PhoneBookService {

    @Autowired
    PhoneBookRepository repository;

    public Optional<PhoneBook> getById(Long id) {
        return repository.findById(id);
    }
    public List<PhoneBook> getAll() {
        return repository.findAll();
    }
    public Optional<PhoneBook> getByUserId(Long userId) {
        return repository.getByUserId(userId);
    }
}
