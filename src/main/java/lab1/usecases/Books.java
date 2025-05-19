package lab1.usecases;

import lombok.Getter;
import lombok.Setter;
import lab1.persistence.BooksDAO;
import lab1.entities.Book;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Model
public class Books {

    @Inject
    private BooksDAO booksDAO;

    @Inject
    private BooksAdded booksAdded;

    @Getter @Setter
    private Book bookToCreate = new Book();

    @Getter
    private List<Book> allBooks;

    @PostConstruct
    public void init(){
        loadAllBooks();
    }

    @Transactional
    public void createBook(){
        booksAdded.incrementBooksAdded();
        this.booksDAO.persist(bookToCreate);
    }

    private void loadAllBooks(){
        this.allBooks = booksDAO.loadAll();
    }
}