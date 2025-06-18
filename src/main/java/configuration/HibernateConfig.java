package configuration;

import entity.City;
import entity.Country;
import entity.Language;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    public static SessionFactory getSessionFactory() {
        return new Configuration()
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Language.class)
                .buildSessionFactory();
    }

}
