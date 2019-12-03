package ru.numbers.demo.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.numbers.demo.entitiy.User;

import java.util.List;
import java.util.Optional;

/**
 * @author Lev_S
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    List<User> findUsersByNameIsContaining (String name, Sort sort);

    @Query("delete from User u where u.id = :id")
    void deleteUserById(@Param("id") Long id);

}
