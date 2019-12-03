package ru.numbers.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.numbers.demo.entitiy.PhoneBook;

import java.util.Optional;

/**
 * @author Lev_S
 */
@Repository
public interface PhoneBookRepository extends JpaRepository<PhoneBook, Long> {

    @Query(value = "select * from phone_book p where p.id in (select a.book_id from users a where a.id = :userId)", nativeQuery = true)
    Optional<PhoneBook> getByUserId (@Param("userId") Long userId);
}
