package ru.numbers.demo.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import ru.numbers.demo.entitiy.Contact;
import ru.numbers.demo.entitiy.PhoneBook;
import ru.numbers.demo.repository.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Lev_S
 */

@SpringBootTest
class ContactServiceTest {

    @Autowired
    ContactService contactService;

    @MockBean
    ContactRepository contactRepository;

    @Test
    void updateContact() {
        Contact contact = new Contact();
        contact.setId(1L);
        PhoneBook phoneBook = new PhoneBook();
        contact.setPhoneBook(phoneBook);

        List<String> errors = new ArrayList<>();

        Contact contactSpy = Mockito.spy(new Contact());

        Mockito.when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        assertDoesNotThrow(() -> contactService.updateContact(1L, contactSpy, errors));

        Mockito.verify(contactRepository, Mockito.times(1)).save(contactSpy);

        Mockito.verify(contactSpy,
                Mockito.times(1)).setId(contact.getId());
        Mockito.verify(contactSpy,
                Mockito.times(1)).setPhoneBook(contact.getPhoneBook());
    }
    @Test
    void updateContactFailedTest() {
        Contact contact = new Contact();
        contact.setId(1L);
        PhoneBook phoneBook = new PhoneBook();
        contact.setPhoneBook(phoneBook);

        List<String> errors = new ArrayList<>();

        Contact contactSpy = Mockito.spy(new Contact());

        Mockito.when(contactRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> contactService.updateContact(1L, contactSpy, errors)
        );
        assertEquals("404 NOT_FOUND \"Contact with such Id doesnt't exist\"", exception.getMessage());

        Mockito.verify(contactRepository,
                Mockito.never()).save(contactSpy);

        Mockito.verify(contactSpy,
                Mockito.never()).setId(Mockito.anyLong());

        Mockito.verify(contactSpy,
                Mockito.never()).setPhoneBook(Mockito.any());
    }

    @Test
    void deleteByIdSuccess() {
        Long id = 1L;
        Mockito.when(contactRepository.findById(id)).thenReturn(Optional.of(new Contact()));

        assertTrue(contactService.deleteById(id));

        Mockito.verify(contactRepository,
                Mockito.times(1)).findById(id);

        Mockito.verify(contactRepository,
                Mockito.times(1)).deleteByContactId(id);
    }

    @Test
    void deleteByIdFailed() {
        Long id = 1L;
        Mockito.when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertFalse(contactService.deleteById(id));

        Mockito.verify(contactRepository,
                Mockito.times(1)).findById(id);

        Mockito.verify(contactRepository,
                Mockito.never()).deleteByContactId(id);
    }

}