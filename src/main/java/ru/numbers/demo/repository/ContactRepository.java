package ru.numbers.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.numbers.demo.entitiy.Contact;

import java.util.List;

/**
 * @author Lev_S
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactByNumber(String number);

    @Modifying
    @Query("delete from Contact a where a.id = :id")
    void deleteByContactId(@Param("id") Long id);

    @Query("select case when c.id is not null then true else false end from Contact c where c.id = :id")
    boolean selectContactCase(@Param("id") Long id);

}
