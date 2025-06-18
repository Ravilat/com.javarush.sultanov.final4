package repository;

import entity.Language;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
@Setter
@Getter
public class LanguageRepository extends AbstractRepository<Language> {
    public LanguageRepository(SessionFactory sessionFactory) {
        super(Language.class, sessionFactory);
    }
    @Override
    public int getAllCount() {
        Query<Long> query = getCurrentSession().createQuery("select count(l) from Language l", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }
}
