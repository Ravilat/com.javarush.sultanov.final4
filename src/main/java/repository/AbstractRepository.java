package repository;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Getter
@Setter
public abstract class AbstractRepository<T> {

    private final Class<T> clazz;

    private final SessionFactory sessionFactory;

    public AbstractRepository(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final int id) {
        return (T) getCurrentSession().find(clazz, id);
    }

    public List<T> getItems(int offset, int count) {
        Query<T> query = getCurrentSession().createQuery("from " + clazz.getName(), clazz);
        query.setMaxResults(count);
        query.setFirstResult(offset);
        return query.list();
    }

    public List<T> getAll() {
        Query<T> query = getCurrentSession().createQuery("from " + clazz.getName(), clazz);
        return query.list();
    }

    public T save(T entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    public T update(T entity) {
        getCurrentSession().merge(entity);
        return entity;
    }

    public T delete(T entity) {
        getCurrentSession().remove(entity);
        return entity;
    }

    public abstract int getAllCount();

    Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
