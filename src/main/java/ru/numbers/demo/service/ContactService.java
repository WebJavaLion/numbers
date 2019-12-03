package ru.numbers.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.exception.ValidateException;
import ru.numbers.demo.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Lev_S
 */

@Service
public class ContactService {

    @Autowired
    ContactRepository repository;

    public Optional<Contact> getById(Long id) {
        return repository.findById(id);
    }

    public List<Contact> getAll() {
        return repository.findAll();
    }

    public void updateContact(Long id, Contact contact, List<String> errors) {
        validateContact(errors);

        Optional<Contact> contactOptional = repository.findById(id);

        if (contactOptional.isPresent()) {
            Contact updatedContact = contactOptional.get();
            contact.setId(updatedContact.getId());
            contact.setPhoneBook(updatedContact.getPhoneBook());

            repository.save(contact);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact with such Id doesnt't exist");
        }

    }

    private void validateContact(List<String> errors) {

        if (!errors.isEmpty()) {
            throw new ValidateException(errors);
        }
    }

    public List<Contact> getContactByNumber(String number) {
        return repository.findContactByNumber(number);
    }

    public boolean deleteById(Long id) {
       Optional<Contact> contactOptional = repository.findById(id);

       if (contactOptional.isPresent()) {
           repository.deleteByContactId(id);
           return true;
       } else {
           return false;
       }
    }
}
