package ru.numbers.demo.entitiy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Lev_S
 */

@Entity
@Table(name = "phone_book")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PhoneBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    Date dateOfCreation;

    @OneToOne(fetch = FetchType.EAGER,
            mappedBy = "phoneBook")
    private User user;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "phoneBook",
            cascade = CascadeType.ALL)
    private List<Contact> contacts;
}
