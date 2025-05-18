package lab1.usecases;

import lab1.mybatis.dao.BooksBookstoresMapper;
import lab1.mybatis.model.Author;
import lab1.mybatis.model.Book;
import lab1.mybatis.model.BooksBookstores;
import lab1.mybatis.model.Bookstore;
import lab1.mybatis.dao.BookMapper;
import lab1.mybatis.dao.BookstoreMapper;
import lab1.mybatis.dao.AuthorMapper;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class BookDetailsMyBatis implements Serializable {

    @Inject
    private BookMapper bookMapper;

    @Inject
    private BookstoreMapper bookstoreMapper;

    @Inject
    private BooksBookstoresMapper booksBookstoresMapper;

    @Inject
    private AuthorMapper authorMapper;

    @Getter @Setter
    private Book book;

    @Getter @Setter
    private long selectedStore;

    @Getter @Setter
    private long selectedAuthor;

    @Getter @Setter
    private List<SelectItem> storeSelections = new ArrayList<>();

    @Getter @Setter
    private List<SelectItem> authorSelections = new ArrayList<>();

    @PostConstruct
    public void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long bookId = Long.parseLong(requestParameters.get("Id"));
        this.book = bookMapper.selectByPrimaryKey(bookId);
        List<Bookstore> bookstores = bookstoreMapper.selectAll();
        for (Bookstore bookstore : bookstores) {
            storeSelections.add(new SelectItem(bookstore.getId(), bookstore.getName()));
        }
        List<Author> authors = authorMapper.selectAll();
        for (Author author : authors) {
            authorSelections.add(new SelectItem(author.getId(), author.getFirstName() + " " + author.getLastName()));
        }
    }

    @Transactional
    public String addStore() {
        BooksBookstores newStore = new BooksBookstores();
        newStore.setBooksId(this.book.getId());
        newStore.setStoresId(this.selectedStore);
        booksBookstoresMapper.insert(newStore);
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
    }

    @Transactional
    public String changeAuthor() {
        this.book.setAuthorId(selectedAuthor);
        bookMapper.updateByPrimaryKey(this.book);
        Author newAuthor = authorMapper.selectByPrimaryKey(selectedAuthor);
        this.book.getAuthor().setId(newAuthor.getId());
        this.book.getAuthor().setFirstName(newAuthor.getFirstName());
        this.book.getAuthor().setLastName(newAuthor.getLastName());
        System.out.println("changed author " + this.book.getAuthor() + " " + this.book.getAuthor().getFirstName());
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
    }
}
