package lab1.persistence;

import lab1.entities.Book;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class BooksDAO {

    @Inject
    private EntityManager em;

    public List<Book> loadAll() {
        return em.createNamedQuery("Book.findAll", Book.class).getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void persist(Book Book){
        this.em.persist(Book);
    }

    public Book findOne(Long id) {
        return em.find(Book.class, id);
    }

    public void update( Book book) {
        em.merge(book);
    }
}
