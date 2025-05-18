package lab1.usecases;

import lab1.entities.Book;
import lab1.entities.Bookstore;
import lab1.persistence.BooksDAO;
import lab1.persistence.BookstoresDAO;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
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

@SessionScoped
@Named
public class BookstoreDetails implements Serializable {

    @Inject
    private BookstoresDAO bookstoresDAO;

    @Getter @Setter
    private Bookstore bookstore;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long storeId = Long.parseLong(requestParameters.get("Id"));
        this.bookstore = bookstoresDAO.findOne(storeId);
    }
}
