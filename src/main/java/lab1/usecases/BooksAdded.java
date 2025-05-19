package lab1.usecases;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@ApplicationScoped
@Named
@Getter
@Setter
public class BooksAdded implements Serializable {
    private int booksAdded = 0;

    public void incrementBooksAdded() {
        booksAdded++;
    }
}
