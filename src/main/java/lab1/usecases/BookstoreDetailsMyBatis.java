package lab1.usecases;

import lab1.mybatis.dao.BookMapper;
import lab1.mybatis.dao.BooksBookstoresMapper;
import lab1.mybatis.model.Book;
import lab1.mybatis.model.BooksBookstores;
import lab1.mybatis.model.Bookstore;
import lab1.mybatis.dao.BookstoreMapper;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class BookstoreDetailsMyBatis implements Serializable {

    @Inject
    private BookstoreMapper bookstoreMapper;

    @Inject
    private BooksBookstoresMapper booksBookstoresMapper;

    @Inject
    private BookMapper bookMapper;

    @Getter @Setter
    private Bookstore bookstore;

    @PostConstruct
    public void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long storeId = Long.parseLong(requestParameters.get("Id"));
        this.bookstore = bookstoreMapper.selectByPrimaryKey(storeId);
    }

    public List<Book> getBooksByStore(Bookstore bookstore){
        List<BooksBookstores> allStores = booksBookstoresMapper.selectAll();
        List<BooksBookstores> filteredStores = allStores.stream()
                .filter(store -> store.getBooksId().equals(bookstore.getId()))
                .collect(Collectors.toList());
        List<Book> books = new ArrayList<>();
        for(BooksBookstores bookRelation: filteredStores){
            books.add(bookMapper.selectByPrimaryKey(bookRelation.getStoresId()));
        }
        return books;
    }
}
