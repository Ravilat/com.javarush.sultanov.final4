package repository;

import entity.City;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CityRepository extends AbstractRepository<City> {
    public CityRepository(SessionFactory sessionFactory) {
        super(City.class, sessionFactory);
    }

    @Override
    public int getAllCount() {
        Query<Long> query = getCurrentSession().createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public List<City> getAllCities() {

        List<City> result = new ArrayList<>();
        int count = getAllCount();
        for (int i = 0; i < count; i = i + 500) {
            result.addAll(getItems(i, 500));
        }
        return result;
    }

    @Override
    public City getById(final int id) {
        Query<City> query = getCurrentSession().createQuery("select c from City c join fetch c.country where c.id=:ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }
}
