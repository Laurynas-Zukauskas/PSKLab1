package lab1.persistence;

import lab1.entities.Book;
import lab1.entities.Bookstore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class BookstoresDAO {

    @Inject
    private EntityManager em;

    public List<Bookstore> loadAll() {
        return em.createNamedQuery("Bookstore.findAll", Bookstore.class).getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void persist(Bookstore bookstore){
        this.em.persist(bookstore);
    }

    public Bookstore findOne(Long id) {
        return em.find(Bookstore.class, id);
    }
}
