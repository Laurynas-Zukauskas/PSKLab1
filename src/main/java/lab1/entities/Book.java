package lab1.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "Book.findAll", query = "select b from Book as b")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String ISBN;

    @ManyToMany
    @JoinTable(name="BOOKS_BOOKSTORES")
    private List<Bookstore> stores = new ArrayList<>();

    @ManyToOne
    private Author author;
}