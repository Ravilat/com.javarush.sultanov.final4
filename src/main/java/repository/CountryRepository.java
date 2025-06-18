package repository;

import entity.Country;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
@Setter
@Getter
public class CountryRepository extends AbstractRepository<Country> {

    public CountryRepository(SessionFactory sessionFactory) {
        super(Country.class, sessionFactory);
    }
    @Override
    public int getAllCount() {
        Query<Long> query = getCurrentSession().createQuery("select count(c) from Country c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }
    @Override
    public List<Country> getAll() {
        Query<Country> query = getCurrentSession().createQuery("select c from Country c join fetch c.languages" , Country.class);
        return query.list();
    }
}
