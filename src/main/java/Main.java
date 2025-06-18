
import configuration.HibernateConfig;
import configuration.RedisConfiguration;
import redis.RedisUtils;
import repository.CityRepository;
import repository.CountryRepository;
import entity.City;
import entity.Country;
import entity.Language;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import redis.CityCountry;

import java.util.List;
import java.util.Set;


import static java.util.Objects.nonNull;

public class Main {

    private final SessionFactory sessionFactory;

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    private final RedisUtils redisUtils;


    public Main() {
        sessionFactory = HibernateConfig.getSessionFactory();

        cityRepository = new CityRepository(sessionFactory);
        countryRepository = new CountryRepository(sessionFactory);

        redisUtils = new RedisUtils(RedisConfiguration.getRedisClient());
    }


    public static void main(String[] args) {
        Main main = new Main();

        try (Session session = main.sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            List<Country> countries = main.countryRepository.getAll();
            List<City> cityList = main.cityRepository.getAllCities();

            List<CityCountry> preparedData = RedisUtils.transformData(cityList);
            main.redisUtils.pushToRedis(preparedData);
            session.getTransaction().commit();
        }

        List<Integer> ids = List.of(56, 66, 103, 888, 1002, 4000, 3823, 2000, 25, 56);

        long startRedis = System.currentTimeMillis();
        main.redisUtils.readDataFromRedis(ids);
        long endRedis = System.currentTimeMillis();

        long startPostgres = System.currentTimeMillis();
        main.testPostgreSQL(ids);
        long endPostgres = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (endRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "PosgreSQL", (endPostgres - startPostgres));

        main.shutdown();
    }

    private void testPostgreSQL(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            for (Integer id : ids) {
                City city = cityRepository.getById(id);
                Set<Language> languages = city.getCountry().getLanguages();
            }
            transaction.commit();
        }
    }


    private void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisUtils.getRedisClient())) {
            redisUtils.getRedisClient().shutdown();
        }
    }
}
