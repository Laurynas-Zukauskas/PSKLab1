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
    private Author author;

    @Getter @Setter
    private List<Bookstore> stores;

    @Getter @Setter
    private Long selectedStoreId;

    @Getter @Setter
    private Long selectedAuthorId;

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
        this.author = authorMapper.selectByPrimaryKey(book.getAuthorId());
        this.stores = getBookstoresByBook(this.book);
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
        newStore.setStoresId(this.selectedStoreId);
        booksBookstoresMapper.insert(newStore);
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
    }

    @Transactional
    public String changeAuthor() {
        this.book.setAuthorId(this.selectedAuthorId);
        bookMapper.updateByPrimaryKey(this.book);
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
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
