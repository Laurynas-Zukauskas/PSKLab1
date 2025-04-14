package lab1.usecases;

import lab1.entities.Author;
import lab1.persistence.AuthorsDAO;
import lab1.persistence.BookstoresDAO;
import lab1.entities.Bookstore;
import lab1.entities.Book;
import lab1.persistence.BooksDAO;

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

@ViewScoped
@Named
public class BookDetails implements Serializable {

    @Inject
    private BooksDAO booksDAO;

    @Inject
    private BookstoresDAO bookstoresDAO;

    @Inject
    private AuthorsDAO authorsDAO;

    @Getter @Setter
    private Book book;

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
        this.book = booksDAO.findOne(bookId);
        List<Bookstore> bookstores = bookstoresDAO.loadAll();
        for (Bookstore bookstore : bookstores) {
            storeSelections.add(new SelectItem(bookstore.getId(), bookstore.getName()));
        }
        List<Author> authors = authorsDAO.loadAll();
        for (Author author : authors) {
            authorSelections.add(new SelectItem(author.getId(), author.getFirstName() + " " + author.getLastName()));
        }
    }

    @Transactional
    public String addStore() {
        List<Bookstore> newStores = this.book.getStores();
        Bookstore selectedStore = bookstoresDAO.findOne(this.selectedStoreId);
        newStores.add(selectedStore);
        this.book.setStores(newStores);
        booksDAO.update(this.book);
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
    }

    @Transactional
    public String changeAuthor() {
        this.book.setAuthor(authorsDAO.findOne(this.selectedAuthorId));
        booksDAO.update(this.book);
        return "book.xhtml?Id=" + this.book.getId() + "&faces-redirect=true";
    }
}
