package lab1.usecases;

import lab1.mybatis.dao.BooksBookstoresMapper;
import lab1.mybatis.dao.BookstoreMapper;
import lab1.mybatis.model.Author;
import lab1.mybatis.model.Book;
import lab1.mybatis.dao.BookMapper;
import lab1.mybatis.model.BooksBookstores;
import lab1.mybatis.model.Bookstore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Model
public class BooksMyBatis {

    @Inject
    private BookMapper bookMapper;

    @Inject
    private BooksAdded booksAdded;

    @Getter @Setter
    private Book bookToCreate = new Book();

    @Getter
    private List<Book> allBooks;

    @Inject
    private BooksBookstoresMapper booksBookstoresMapper;
    @Inject
    private BookstoreMapper bookstoreMapper;

    @PostConstruct
    public void init(){
        loadAllBooks();
        System.out.println("BooksMyBatis init");
        for (Book book : allBooks) {
            System.out.println("book " + book.getId() + " |" + book.getTitle() + "| " + book.getIsbn());
            System.out.println("stores " + book.getBookstores());
            Author author = book.getAuthor();
            if (author != null) {
                System.out.println("author "  + book.getAuthorId() + " " + author + " " + author.getFirstName() + " " + author.getLastName() + " " + author.getId());
            }
        }
    }

    @Transactional
    public void createBook(){
        booksAdded.incrementBooksAdded();
        this.bookMapper.insert(bookToCreate);
    }

    private void loadAllBooks(){
        this.allBooks = bookMapper.selectAll();
    }

    public List<Bookstore> getBookstoresByBook(Book book){
        List<BooksBookstores> allStores = booksBookstoresMapper.selectAll();
        List<BooksBookstores> filteredStores = allStores.stream()
                .filter(store -> store.getBooksId().equals(book.getId()))
                .collect(Collectors.toList());
        List<Bookstore> bookstores = new ArrayList<>();
        for(BooksBookstores bookstore: filteredStores){
            bookstores.add(bookstoreMapper.selectByPrimaryKey(bookstore.getStoresId()));
        }
        return bookstores;
    }
}